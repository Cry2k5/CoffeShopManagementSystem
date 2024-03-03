package controller;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.Customer;
import model.JDBCUtil;
import model.Product;
import model.data;

public class homeController implements Initializable {

	@FXML
	private Button customers_btn;

	@FXML
	private TextField inven_Name;

	@FXML
	private TextField inven_Price;

	@FXML
	private TextField inven_ProductID;

	@FXML
	private TextField inven_Stock;
	
	@FXML
	private TextField inven_Search;
	
	@FXML
	private Button inven_searchBtn;

	@FXML
	private ComboBox<String> inven_Type;

	@FXML
	private Button inven_addBtn;

	@FXML
	private Button inven_clearBtn;

	@FXML
	private TableColumn<Product, String> inven_col_Date;

	@FXML
	private TableColumn<Product, String> inven_col_ProductID;

	@FXML
	private TableColumn<Product, String> inven_col_Name;

	@FXML
	private TableColumn<Product, String> inven_col_Price;

	@FXML
	private TableColumn<Product, String> inven_col_Stock;

	@FXML
	private TableColumn<Product, String> inven_col_Type;

	@FXML
	private Button inven_deleteBtn;

	@FXML
	private ImageView inven_image;

	@FXML
	private Button inven_importBtn;

	@FXML
	private Button inven_updateBtn;

	@FXML
	private Button inventory_btn;

	@FXML
	private AnchorPane inventory_form;

	@FXML
	private TableView<Product> inventory_table;

	@FXML
	private Button logout_btn;

	@FXML
	private AnchorPane main_form;

	@FXML
	private Button menu_btn;


    @FXML
    private TableColumn<Product, String> menu_col_price;

    @FXML
    private TableColumn<Product, String> menu_col_productName;

    @FXML
    private TableColumn<Product, String> menu_col_quantity;

    @FXML
    private AnchorPane menu_form;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private Button menu_payBtn;

    @FXML
    private Button menu_receiptBtn;

    @FXML
    private Button menu_removeBtn;

    @FXML
    private ScrollPane menu_scrollPane;

    @FXML
    private TableView<Product> menu_table;

    @FXML
    private Label menu_total;
    

    @FXML
    private TableColumn<Customer, String> customers_col_ID;

    @FXML
    private TableColumn<Customer, String> customers_col_cashier;

    @FXML
    private TableColumn<Customer, String> customers_col_date;

    @FXML
    private TableColumn<Customer, String> customers_col_totals;

    @FXML
    private AnchorPane customers_form;

    @FXML
    private TableView<Customer> customers_table;
	
	private Alert alert;

	private Connection connect;
	private PreparedStatement prepare;
	private ResultSet result;
	private Statement statement;

	private Image image;

//	private String[] typeList = {"The Basics","Specialities","Cream on top","Tea"};

//	public void invenTypeList() {
//		List<String> typeL = new ArrayList<>();
//		
//		for(String data: typeList) {
//			typeL.add(data);
//		}

//		ObservableList<String> listData = FXCollections.observableArrayList(typeL);
//		inven_Type.setItems(listData);

//		inven_Type.getSelectionModel().selectFirst();
//	}
	

	public void addBtn() throws Exception {

		if (inven_ProductID.getText().isEmpty() 
				|| inven_Name.getText().isEmpty() 
				|| inven_Stock.getText().isEmpty()
				|| inven_Price.getText().isEmpty() 
				|| inven_Type.getSelectionModel().getSelectedItem() == null
				|| data.path == null) {

			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Message");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all blank fields!");
			alert.showAndWait();

		} else {
			String checkProdID = "SELECT product_id FROM product WHERE product_id = '" + inven_ProductID.getText()+ "'";

			connect = JDBCUtil.getConnection();

			statement = connect.createStatement();

			result = statement.executeQuery(checkProdID);

			if (result.next()) {

				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText(inven_ProductID.getText() + " is already taken!");
				alert.showAndWait();

			} else {
				String insert = "INSERT INTO product " + "(product_id, product_name, stock, type, price, image, date) "
						+ "VALUES(?,?,?,?,?,?,?)";

				prepare = connect.prepareStatement(insert);
				prepare.setString(1, inven_ProductID.getText());
				prepare.setString(2, inven_Name.getText());
				prepare.setString(3, inven_Stock.getText());
				prepare.setString(4, (String) inven_Type.getSelectionModel().getSelectedItem());
				prepare.setString(5, inven_Price.getText());

				String path = data.path;
				path = path.replace("\\", "\\\\");

				prepare.setString(6, path);

				Date date = new Date();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				prepare.setString(7, String.valueOf(sqlDate));

				prepare.executeUpdate();

				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Message");
				alert.setHeaderText(null);
				alert.setContentText("Successfully Added!");
				alert.showAndWait();

				invenShowData();

				clearBtn();

			}

		}
	}
	
	public void searchBtn() throws Exception{
		invenShowData();
		
        FilteredList<Product> filteredList = new FilteredList<>(invenListData, b -> true);
        inven_Search.textProperty().addListener((observableValue, s, t1) -> {
            filteredList.setPredicate(product -> {
                if (t1 == null || t1.isEmpty()) {
                    return true;
                }
                String lowerCaseSearch = t1.toLowerCase();


                if(product.getProductId().toLowerCase().indexOf(lowerCaseSearch) != -1) {
                    return true;
                }
                else if(product.getProductName().toLowerCase().indexOf(lowerCaseSearch) != -1) {
                    return true;
                }
                else if(String.valueOf(product.getStock()).toLowerCase().contains(lowerCaseSearch)) {
                    return true;
                }
                else if(product.getType().toLowerCase().indexOf(lowerCaseSearch) != -1) {
                    return true;
                }
                else if (String.valueOf(product.getPrice()).toLowerCase().contains(lowerCaseSearch)) {
                    return true;
                }
                else if (String.valueOf(product.getDate()).toLowerCase().contains(lowerCaseSearch)) {
                    return true;
                }
                else {
                    return false;
                }
            });
        });
        SortedList<Product> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(inventory_table.comparatorProperty());
        inventory_table.setItems(sortedList);
	}

	public void updateBtn() throws Exception {

		if (inven_ProductID.getText().isEmpty() 
				|| inven_Name.getText().isEmpty() 
				|| inven_Stock.getText().isEmpty()
				|| inven_Price.getText().isEmpty() 
				|| inven_Type.getSelectionModel().getSelectedItem() == null
				|| data.path == null
				|| data.id == 0 ) {

			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Message");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all blank fields!");
			alert.showAndWait();

		} else {
			String path = data.path;
			path = path.replace("\\", "\\\\");

			 String updateData = "UPDATE product SET " 
			 + "product_id = '" + inven_ProductID.getText() +"', product_name = '"
			 + inven_Name.getText()+"', type ='"
			 + inven_Type.getSelectionModel().getSelectedItem()+"', stock = '"
			 + inven_Stock.getText()+"', price = '"+ inven_Price.getText()+"', image = '"
			 + path + "', date = '"+ data.date + "' WHERE id = " + data.id;
			 
//			String updateData = "UPDATE product SET product_id = ?, product_name = ?, stock = ?, type = ?, price = ?, image = ?, date = ?";

			connect = JDBCUtil.getConnection();

			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Information Message");
			alert.setHeaderText(null);
			alert.setContentText("Do you want to UPDATE ProductID: " + inven_ProductID.getText() + "?");
			Optional<ButtonType> option = alert.showAndWait();

			if (option.get().equals(ButtonType.OK)) {

				prepare = connect.prepareStatement(updateData);

//				prepare.setString(1, inven_ProductID.getText());
//				prepare.setString(2, inven_Name.getText());
//				prepare.setString(3, inven_Stock.getText());
//				prepare.setString(4, inven_Type.getSelectionModel().getSelectedItem());
//				prepare.setString(5, inven_Price.getText());
//				prepare.setString(6, path);
//				prepare.setString(7, data.date);

				prepare.executeUpdate();

				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Message");
				alert.setHeaderText(null);
				alert.setContentText("Successfully Updated!");
				alert.showAndWait();

				// Hiện thị lại cái đã caạp nhật lên bảng.
				invenShowData();
				// Đặt lại giá trị rỗng cho các label.
				clearBtn();

			} 
		}
	}

	public void deleteBtn() throws Exception {

		if (data.id==0) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Message");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all blank fields!");
			alert.showAndWait();
		} else {
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Information Message");
			alert.setHeaderText(null);
			alert.setContentText("Do you want to DELETE ProductID: " + inven_ProductID.getText() + "?");
			Optional<ButtonType> option = alert.showAndWait();

			if (option.get().equals(ButtonType.OK)) {
				String deleteData = "DELETE FROM product WHERE id = " + data.id;
				prepare = connect.prepareStatement(deleteData);
				prepare.executeUpdate();

				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Message");
				alert.setHeaderText(null);
				alert.setContentText("Successfully Deleted!");
				alert.showAndWait();

				// Hiện thị lại dữ liệu lên bảng.
				invenShowData();
				// Đặt lại giá trị rỗng cho các label.
				clearBtn();
			} 
		}

	}

	public void clearBtn() {

		// if(event.getSource()==inven_clearBtn) {
		inven_ProductID.setText("");
		inven_Name.setText("");
		inven_Stock.setText("");
		inven_Type.getSelectionModel().clearSelection();
		inven_Price.setText("");

		data.path = "";
		data.id = 0;
		inven_image.setImage(null);

	}

	// }

	public void importBtn(ActionEvent event) {

		FileChooser openFileChooser = new FileChooser();
		openFileChooser.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png", "*jpg", "*jpeg"));

		File file = openFileChooser.showOpenDialog(main_form.getScene().getWindow());

		if (file != null) {
			
			data.path = file.getAbsolutePath();

			image = new Image(file.toURI().toString(), 120, 130, false, true);

			inven_image.setImage(image);
		}
	}

	// Truy vấn cơ sở dữ liệu và trả về một danh sách chứa các sản phẩm.
	public ObservableList<Product> invenDataList() throws Exception {

		ObservableList<Product> listData = FXCollections.observableArrayList();

		String sql = "SELECT * FROM product";

		connect = JDBCUtil.getConnection();

		prepare = connect.prepareStatement(sql);

		result = prepare.executeQuery();

		Product product;

		while (result.next()) {
			product = new Product(result.getInt("id"),
					result.getString("product_id"),
					result.getString("product_name"),
					result.getString("type"),
					result.getInt("stock"),
					result.getDouble("price"),
					result.getString("image"),
					result.getDate("date"));

			listData.add(product);
		}
		return listData;
	}

	// Hiển thị dữ liệu ra Bảng
	private ObservableList<Product> invenListData;

	public void invenShowData() throws Exception {
		invenListData = invenDataList();

		inven_col_ProductID.setCellValueFactory(new PropertyValueFactory<>("productId"));
		inven_col_Name.setCellValueFactory(new PropertyValueFactory<>("productName"));
		inven_col_Stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
		inven_col_Type.setCellValueFactory(new PropertyValueFactory<>("type"));
		inven_col_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
		inven_col_Date.setCellValueFactory(new PropertyValueFactory<>("date"));

		inventory_table.setItems(invenListData);

	}

	public void invenSelectData() {

		Product product = inventory_table.getSelectionModel().getSelectedItem();
		int num = inventory_table.getSelectionModel().getSelectedIndex();

		if ((num - 1) < -1) return;

		inven_ProductID.setText(product.getProductId());
		inven_Name.setText(product.getProductName());
		inven_Stock.setText(String.valueOf(product.getStock()));
		inven_Price.setText(String.valueOf(product.getPrice()));

		data.path = product.getImage();
		String path = "File:" + product.getImage();
		data.date = String.valueOf(product.getDate());
		data.id = product.getId();
		image = new Image(path, 120, 130, false, true);

		inven_image.setImage(image);

	}

	private String[] typeList = {"The Basics", "Specialities", "Cream on top", "Tea"};
	public void invenTypeList() {
		List<String> typeL = new ArrayList<>();
		for(String data: typeList) {
			typeL.add(data);
		}
		
		ObservableList<String> listData = FXCollections.observableArrayList(typeL);
		inven_Type.setItems(listData);
	}

	public ObservableList<Product> menuGetData() throws Exception{
		
		String sql = "SELECT * FROM product";
		
		ObservableList<Product> listData = FXCollections.observableArrayList();
		
		connect = JDBCUtil.getConnection();
		
		prepare = connect.prepareStatement(sql);
		
		result = prepare.executeQuery();
		
		Product product;
		
		while(result.next()) {
			
			product = new Product( result.getInt("id"),
					result.getString("product_id"),
					result.getString("product_name"),
					result.getString("type"),
					result.getInt("stock"),
					result.getDouble("price"),
					result.getString("image"),
					result.getDate("date"));
			
			listData.add(product);

		}
		
		return listData;
		
	}
	
	private ObservableList<Product> cardList = FXCollections.observableArrayList();

	public void menuDisplayCard() throws Exception {
		
		cardList.clear();
		cardList.addAll(menuGetData());
		
		int row = 0;
		int col = 0;
		
		menu_gridPane.getChildren().clear();
		menu_gridPane.getRowConstraints().clear();
		menu_gridPane.getColumnConstraints().clear();
		
		for(int q =0; q < cardList.size(); q++) {
			
			FXMLLoader load = new FXMLLoader();
			load.setLocation(getClass().getResource("/view/cardProduct.fxml"));
			AnchorPane pane = load.load();
			cardProductController cardController = load.getController();
			cardController.setData(cardList.get(q));
			
			if(col == 4) {
				col = 0;
				row ++;
			}
			
			menu_gridPane.add(pane, col++, row);

			GridPane.setMargin(pane, new Insets(10));
			
		}
	}
	
	
	public ObservableList<Product> menuGetOrder() throws Exception{
		
		customerID();
		
		ObservableList<Product> listData = FXCollections.observableArrayList();
		
		String sql = "SELECT * FROM customers WHERE customer_id = " + cID;
		
		connect = JDBCUtil.getConnection();
		
		prepare = connect.prepareStatement(sql);
		
		result = prepare.executeQuery();
		
		Product product;
		
		while(result.next()) {
			product = new Product(result.getInt("id"),
					result.getString("product_id"),
					result.getString("product_name"),
					result.getInt("quantity"),
					result.getString("type"),
					result.getDouble("price"),
					result.getString("image"),
					result.getDate("date"));
			listData.add(product);
		}
		
		
		return listData;
		
		
	}
	
	private ObservableList<Product> menuOrderListData;
	public void menuShowOrderData() throws Exception{
		
		menuOrderListData = menuGetOrder();
		
		menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
		menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		menu_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

		menu_table.setItems(menuOrderListData);
		
	}
	
	private int getId;
	public void menuSelectOrder() {
		Product product = menu_table.getSelectionModel().getSelectedItem();
		
		int num = menu_table.getSelectionModel().getSelectedIndex();
		
		if((num-1) < -1) return;
		
		//Lấy đối tượng trong bảng.
		getId = product.getId();
		
	}
	
	private double totalPr;
	public void menuGetTotal() throws Exception{
		customerID();
		String total = "SELECT SUM(price) FROM customers WHERE customer_id = " + cID;
		
		
		connect = JDBCUtil.getConnection();
		
		prepare = connect.prepareStatement(total);
		
		result = prepare.executeQuery();
		
		if(result.next()) {
			
			totalPr = result.getDouble("SUM(price)");
		}
		
	}
	
	public void menuDisplayTotal() throws Exception{
		menuGetTotal();
		menu_total.setText("$"+ totalPr);
	}
	
	public void payBtn() throws Exception {
		
		
		if(totalPr==0) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Message");
			alert.setHeaderText(null);
			alert.setContentText("Please choose your order first!");
			alert.showAndWait();
		}else {
			
			menuGetTotal();
			
			String insertPay = "INSERT INTO receipt (customer_id, total, date, employee_email)"
					+ "VALUES(?,?,?,?)";
			
			connect = JDBCUtil.getConnection();
			
			
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Message");
			alert.setHeaderText(null);
			alert.setContentText("Do you want to PAY this receipt?");
			Optional<ButtonType> option = alert.showAndWait();

			if (option.get().equals(ButtonType.OK)) {
				
				customerID();
				menuGetTotal();
				
				prepare = connect.prepareStatement(insertPay);
				
				prepare.setString(1, String.valueOf(cID));
				
				prepare.setString(2, String.valueOf(totalPr));
				
				Date date = new Date();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				prepare.setString(3, String.valueOf(sqlDate));
				
				prepare.setString(4,  data.employee_email);

				prepare.executeUpdate();
				
				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Message");
				alert.setHeaderText(null);
				alert.setContentText("Successfull!");
				alert.showAndWait();
				
				
				menuShowOrderData();
				
				menuRestart();
				
			
			}else {
				alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning Message");
				alert.setHeaderText(null);
				alert.setContentText("Cancelled!");
				alert.showAndWait();
				
			}
			
		}
	}
	
	public void removeBtn() throws Exception {
		
		if(getId == 0) {
			
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Message");
			alert.setHeaderText(null);
			alert.setContentText("Please select the items to remove first!");
			alert.showAndWait();
			
		}else {
			String deleteData = "DELETE FROM customers WHERE id = "+ getId;
			
			connect = JDBCUtil.getConnection();
			
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Message");
			alert.setHeaderText(null);
			alert.setContentText("Do you want to REMOVE this order?");
			Optional<ButtonType> option = alert.showAndWait();
			
			if (option.get().equals(ButtonType.OK)) {

				prepare = connect.prepareStatement(deleteData);
				prepare.executeUpdate();
			
			}
			
			menuShowOrderData();
		}
		
	}
	
	public void menuRestart() {
		totalPr = 0;
		menu_total.setText("$0.0");
	}
	
	private int cID;
	public void customerID() throws Exception{
		
		String sql = "SELECT MAX(customer_id) FROM customers";
		connect = JDBCUtil.getConnection();
		
		prepare = connect.prepareStatement(sql);
		result = prepare.executeQuery();
		
		if(result.next()) {
			cID = result.getInt("MAX(customer_id)");
		}
		
		String checkCID = "SELECT MAX(customer_id) FROM receipt";
		prepare = connect.prepareStatement(checkCID);
		
		result = prepare.executeQuery();
		
		int checkID = 0;
		if(result.next()) {
			checkID = result.getInt("MAX(customer_id)");
		}
		
		if(cID == 0) {
			cID +=1;
		}else if(cID == checkID) {
			cID += 1;
		}
		
		data.cID = cID;
	}
	
	public ObservableList<Customer> customerDataList() throws Exception{
		ObservableList<Customer> listData = FXCollections.observableArrayList();
		
		String sql = "SELECT * FROM receipt";
		
		connect = JDBCUtil.getConnection();
		
		prepare = connect.prepareStatement(sql);
		
		result = prepare.executeQuery();
		
		Customer customer;
		
		while(result.next()) {
			customer = new Customer(result.getInt("id"),
					result.getInt("customer_id"),
					result.getDouble("total"),
					result.getDate("date"),
					result.getString("employee_email"));
		
			listData.add(customer);
		}
		
		return listData;
	}
	
	private ObservableList<Customer> customerListData;
	public void customerShowData() throws Exception {
		customerListData = customerDataList();
		
		customers_col_ID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
		customers_col_totals.setCellValueFactory(new PropertyValueFactory<>("total"));
		customers_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
		customers_col_cashier.setCellValueFactory(new PropertyValueFactory<>("employee_email"));

		customers_table.setItems(customerListData);
		
		
	}
	
	public void switchForm(ActionEvent event) throws Exception {
		
		if(event.getSource()== inventory_btn) {
			inventory_form.setVisible(true);
			menu_form.setVisible(false);
			customers_form.setVisible(false);
			invenTypeList();
			invenShowData();
		}
		else if(event.getSource()== menu_btn) {
			inventory_form.setVisible(false);
			menu_form.setVisible(true);
			customers_form.setVisible(false);
			menuDisplayCard();
			menuDisplayTotal();
			menuShowOrderData();
		}
		else if(event.getSource()==customers_btn) {
			
			inventory_form.setVisible(false);
			menu_form.setVisible(false);
			customers_form.setVisible(true);
			
			customerShowData();
			
		}
		
	}
	
	//Login button
	public void logout(ActionEvent event) throws Exception {

		if (event.getSource() == logout_btn) {

			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Information Message");
			alert.setHeaderText(null);
			alert.setContentText("Do you want to log out?");
			
			Optional<ButtonType> option = alert.showAndWait();

			if (option.get().equals(ButtonType.OK)) {

				// Khi xác nhận thoát, sẽ ẩn đi giao diện chính.
				logout_btn.getScene().getWindow().hide();

				// Trở về giao diện đăng nhập.
				Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));

				Stage stage = new Stage();
				Scene scene = new Scene(root);

				stage.setTitle("Anh Coffee Shop Management System");
				stage.setScene(scene);
				stage.show();

			}
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1){

		invenTypeList();

		try {
			searchBtn();
			
			invenShowData();
	
			menuDisplayCard();
	
			menuGetOrder();
		
			menuDisplayTotal();
		
			menuShowOrderData();
	
			customerShowData();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
