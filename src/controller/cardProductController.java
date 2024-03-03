package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.JDBCUtil;
import model.Product;
import model.data;

public class cardProductController implements Initializable{

    @FXML
    private Button card_addbtn;

    @FXML
    private AnchorPane card_form;

    @FXML
    private ImageView card_image;

    @FXML
    private Label card_prodName;

    @FXML
    private Label card_prodPrice;

    @FXML
    private Spinner<Integer> card_spinner;
    
    private Connection connect;
	private PreparedStatement prepare;
	private ResultSet result;
	
	private Alert alert;
	
	private String prod_type;
	
	private String prod_id;
	
	private String prod_image;
	
	private String prod_Date;
	
	private Image image;
	
	private double total;
	
	private double price;
	
	private int qty;

	private SpinnerValueFactory<Integer> spin;
	
	public void setData(Product product) {
		
		prod_id = product.getProductId();		
		prod_type = product.getType();
		prod_image = product.getImage();
		prod_Date = String.valueOf(product.getDate());		
		card_prodName.setText(product.getProductName());
		card_prodPrice.setText("$"+String.valueOf(product.getPrice()));
		
		String path = "File:"+ product.getImage();
		image = new Image(path, 223, 210, false, true);
		card_image.setImage(image);
		price = product.getPrice();
		
	}
    
	
	public void setQuantity() {
		spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,  100, 0);
		card_spinner.setValueFactory(spin);
		
	}
	
	public void addBtn() throws Exception {
		
		
		homeController home = new homeController();
		home.customerID();
		qty = card_spinner.getValue();
				
		if(qty == 0) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Message");
			alert.setHeaderText(null);
			alert.setContentText("Quantity is not empty!");
			alert.showAndWait();
		}else {
			
			int checkSt = 0;
			
			String checkStock = "SELECT stock FROM product WHERE product_id = '"
					+ prod_id +"'";
			
			connect = JDBCUtil.getConnection();

			
			prepare = connect.prepareStatement(checkStock);
			
			result = prepare.executeQuery();
			
			if(result.next()) {
				checkSt = result.getInt("stock");
			}
			
			if(checkSt < qty) {
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Out of stock!");
				alert.showAndWait();
			}
			else {
				
			prod_image = prod_image.replace("\\", "\\\\");

				
			String insertData = "INSERT INTO customers (customer_id, product_id, product_name, quantity, type, price, image, date, employee_email) " + "VALUES(?,?,?,?,?,?,?,?,?)";
		
			prepare = connect.prepareStatement(insertData);
			prepare.setString(1, String.valueOf(data.cID));
			
			
			prepare.setString(2, prod_id);
			
			
			prepare.setString(3, card_prodName.getText());
			prepare.setString(4, String.valueOf(qty));
			prepare.setString(5, prod_type);

			total = (qty * price);
			prepare.setString(6, String.valueOf(total));
			
			prepare.setString(7, prod_image);

			
			java.util.Date date = new java.util.Date();
			java.sql.Date sqlDate= new java.sql.Date(date.getTime());
			prepare.setString(8, String.valueOf(sqlDate));
			prepare.setString(9, String.valueOf(data.employee_email));
			
			prepare.executeUpdate();
			
			int upStock = checkSt - qty;
						
			String updateStock = "UPDATE product SET product_name = '"
					+ card_prodName.getText()+ "', stock = "+ upStock+", type = '"
					+ prod_type + "', price = " + price
					+ ", image = '"+ prod_image+"', date = '" + prod_Date + "' WHERE product_id = '" + prod_id +"'";
			
			prepare = connect.prepareStatement(updateStock);
			prepare.executeUpdate();
			
			
			
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Message");
			alert.setHeaderText(null);
			alert.setContentText("Successfully Added!");
			alert.showAndWait();
			
			home.menuGetTotal();
			}						
			
		}
	}
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setQuantity();
	}

}
