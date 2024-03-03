package controller;

import java.util.Date;
import java.util.regex.Pattern;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;

public class loginController {

	@FXML
	private Label asktocreate;
	
    @FXML
    private Label signinhere;
	
    @FXML
    private Button fo_back1;

    @FXML
    private Button fo_back2;

    @FXML
    private Button fo_changepassword;

    @FXML
    private PasswordField fo_confirm;

    @FXML
    private Button fo_continue;

    @FXML
    private TextField fo_email;

    @FXML
    private AnchorPane fo_enternewpassForm;

    @FXML
    private AnchorPane fo_forgetpasswordForm;

    @FXML
    private PasswordField fo_newpassword;

    @FXML
    private TextField fo_phone;

    @FXML
    private Button home_already;

    @FXML
    private Button home_createbtn;

    @FXML
    private AnchorPane home_form;

    @FXML
    private TextField lo_email;

    @FXML
    private Hyperlink lo_forgot;

    @FXML
    private AnchorPane lo_form;

    @FXML
    private Button lo_loginbtn;

    @FXML
    private PasswordField lo_password;

    @FXML
    private TextField re_email;

    @FXML
    private TextField re_firstname;

    @FXML
    private AnchorPane re_form;

    @FXML
    private TextField re_lastname;

    @FXML
    private PasswordField re_password;

    @FXML
    private TextField re_phone;

    @FXML
    private Button re_signupbtn;
    
    @FXML
    private AnchorPane verifyEmail_form;
    
    @FXML
    private Button re_OKBtn;

    @FXML
    private TextField re_OTP;

	private Connection connect;
	private PreparedStatement prepare;
	private ResultSet result;

	private Alert alert;

	
	//login button
	public void loginBtn(ActionEvent event) throws Exception{

		
		//check the email or password feild do write yet? if not, give notification error!
		if (event.getSource() == lo_loginbtn) {

			if (lo_email.getText().isEmpty() || lo_password.getText().isEmpty()) {
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Please enter email/password!");
				alert.showAndWait();
			} 
			else {
				String selectData = "SELECT email, password FROM employee WHERE email = ? and password = ?";

				connect = JDBCUtil.getConnection();

				
					prepare = connect.prepareStatement(selectData);
					prepare.setString(1, lo_email.getText());
					prepare.setString(2, encodePassword(lo_password.getText()));

					result = prepare.executeQuery();

					// Nếu đăng nhập thành công sẽ chuyển sang giao diện chính của chương trình
					if (result.next()) {

						data.employee_email = lo_email.getText();
						
						alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information Message");
						alert.setHeaderText(null);
						alert.setContentText("Successfully Login!");
						alert.showAndWait();
						
						
						//Kết nối với giao diện chính khi đăng nhập thành công.
						Parent root = FXMLLoader.load(getClass().getResource("/view/home.fxml"));
						
						Stage stage = new Stage();
						Scene scene = new Scene(root);
						
						
						Image image = new Image(getClass().getResourceAsStream("/view/images/cafe.png"));
						stage.getIcons().add(image);
						stage.setTitle("Anh Coffee Shop Management System");
						
						stage.setScene(scene);
						stage.show();
						
						lo_loginbtn.getScene().getWindow().hide();

					} else {
						// Nếu không sẽ xuất hiện thông báo
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Message");
						alert.setHeaderText(null);
						alert.setContentText("Incorrect Username/Password !");
						alert.showAndWait();

					}
				} 
			}

		}

	//register button
	public void reBtn(ActionEvent event) throws Exception{

		if (event.getSource() == re_signupbtn) {
			if (re_email.getText().isEmpty() || re_password.getText().isEmpty() || re_firstname.getText().isEmpty()
					|| re_lastname.getText().isEmpty() || re_phone.getText().isEmpty()) {

				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Please fill all blank fields!");
				alert.showAndWait();

			} else {
				String reData = "INSERT INTO employee(email, password, first_name, last_name, phone, date)"
						+ "VALUES(?,?,?,?,?,?)";
				connect = JDBCUtil.getConnection();


					// Kiểm tra nếu email đã tồn tài.
					String checkEmail = "SELECT email FROM employee WHERE email = '" + re_email.getText() + "'";

					prepare = connect.prepareStatement(checkEmail);
					result = prepare.executeQuery();
					
					
					String email_patern = "^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$";

					if (result.next()) {
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Message");
						alert.setHeaderText(null);
						alert.setContentText(re_email.getText() + " is already taken");
						alert.showAndWait();
					}

					// Kiểm tra độ dài kí tự mật khâu đăng kí
					else if (re_password.getText().length() < 4) {
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Message");
						alert.setHeaderText(null);
						alert.setContentText("The password must be at least 5 characters long!");
						alert.showAndWait();
					}
					
					
					else if(Pattern.matches(email_patern, re_email.getText())== false)
					{
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Message");
						alert.setHeaderText(null);
						alert.setContentText("The email INVALID: example@gmail.com");
						alert.showAndWait();
					}
					else {
						prepare = connect.prepareStatement(reData);
						
						
						prepare.setString(1, re_email.getText());
						
						prepare.setString(2, encodePassword(re_password.getText()));
						prepare.setString(3, re_firstname.getText());
						prepare.setString(4, re_lastname.getText());
						prepare.setString(5, re_phone.getText());

						Date date = new Date();

						java.sql.Date sqlDate = new java.sql.Date(date.getTime());
						prepare.setString(6, String.valueOf(sqlDate));

						prepare.executeUpdate();

						alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information Message");
						alert.setHeaderText(null);
						alert.setContentText("Successfully registered Account!");
						alert.showAndWait();

						re_email.setText("");
						re_password.setText("");
						re_firstname.setText("");
						re_lastname.setText("");
						re_phone.setText("");

						TranslateTransition slider = new TranslateTransition();

						slider.setNode(home_form);
						slider.setToX(0);
						slider.setDuration(Duration.seconds(.5));

						slider.setOnFinished((ActionEvent e) -> {
							home_already.setVisible(false);
							home_createbtn.setVisible(true);
						});

						slider.play();
					}

				} 
			}
		}
	
	public static String encodePassword(String password)
	{
		try {
			
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encodeHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder hexString = new StringBuilder();
			for(byte b : encodeHash) {
				String hex = String.format("%02x", b);
				hexString.append(hex);
			}
			
			return hexString.toString();
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public void switchForgetPass() {

		fo_forgetpasswordForm.setVisible(true);
		lo_form.setVisible(false);

	}

	public void continueBtn(ActionEvent event) {

		if (event.getSource() == fo_continue) {

			if (fo_email.getText().isEmpty() || fo_phone.getText().isEmpty()) {
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Please fill all blank fields!");
				alert.showAndWait();

			} else {

				String selectData = "SELECT email, phone FROM employee WHERE email = ? AND phone = ?";
				connect = JDBCUtil.getConnection();

				try {

					prepare = connect.prepareStatement(selectData);

					prepare.setString(1, fo_email.getText());
					prepare.setString(2, fo_phone.getText());

					result = prepare.executeQuery();

					if (result.next()) {

						fo_enternewpassForm.setVisible(true);
						fo_forgetpasswordForm.setVisible(false);

					} else {
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Message");
						alert.setHeaderText(null);
						alert.setContentText("The information is invalid!");
						alert.showAndWait();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

	}

	public void changePassBtn(ActionEvent event) {

		if (event.getSource() == fo_changepassword) {
			if (fo_newpassword.getText().isEmpty() || fo_confirm.getText().isEmpty()) {
				
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Please fill all blank fields!");
				alert.showAndWait();
				
			} else {

				if (fo_newpassword.getText().equals(fo_confirm.getText())) {
					String getDate = "SELECT date FROM employee WHERE email = '" + fo_email.getText()+"'";

					connect = JDBCUtil.getConnection();

					try {
						prepare = connect.prepareStatement(getDate);
						result = prepare.executeQuery();

						String date = "";
						if (result.next()) {
							date = result.getString("date");
						}

						String updatePass = "UPDATE employee SET password ='" + encodePassword(fo_newpassword.getText()) + "', phone ='"
								+ fo_phone.getText() + "', date='" + date + "' WHERE email = '" + fo_email.getText()
								+ "'";
						
						prepare = connect.prepareStatement(updatePass);
						prepare.executeUpdate();
						
						alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information Message");
						alert.setHeaderText(null);
						alert.setContentText("Successfully Changed Password!");
						alert.showAndWait();
						
						lo_form.setVisible(true);
						fo_enternewpassForm.setVisible(false);
						
						//Đặt lại thông tin rỗng
						fo_newpassword.setText("");
						fo_confirm.setText("");
						fo_email.setText("");
						fo_phone.setText("");
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Message");
					alert.setHeaderText(null);
					alert.setContentText("NOT MATCH!");
					alert.showAndWait();
				}

			}
		}

	}

	public void back1(ActionEvent event) {
		
		if(event.getSource()==fo_back1) {
			lo_form.setVisible(true);
		fo_forgetpasswordForm.setVisible(false);
		}
		
	}
	
	public void back2(ActionEvent event) {
		
		if(event.getSource()==fo_back2) {
			fo_forgetpasswordForm.setVisible(true);
			fo_enternewpassForm.setVisible(false);
		}
		
	}
	
	public void switchForm(ActionEvent event) {

		TranslateTransition slider = new TranslateTransition();

		if (event.getSource() == home_createbtn) {

			slider.setNode(home_form);
			slider.setToX(300);
			slider.setDuration(Duration.seconds(.5));

			slider.setOnFinished((ActionEvent e) -> {
				home_already.setVisible(true);
				signinhere.setVisible(true);
				home_createbtn.setVisible(false);
				asktocreate.setVisible(false);

				fo_forgetpasswordForm.setVisible(false);
				lo_form.setVisible(true);
				fo_enternewpassForm.setVisible(false);
			});

			slider.play();

		} else if (event.getSource() == home_already) {
			slider.setNode(home_form);
			slider.setToX(0);
			slider.setDuration(Duration.seconds(.5));

			slider.setOnFinished((ActionEvent e) -> {
				home_already.setVisible(false);
				signinhere.setVisible(false);
				home_createbtn.setVisible(true);
				asktocreate.setVisible(true);

				fo_forgetpasswordForm.setVisible(false);
				lo_form.setVisible(true);
				fo_enternewpassForm.setVisible(false);
			});

			slider.play();
		}
	}

}
