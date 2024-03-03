package model;

import java.sql.Date;

public class Product {
	private Integer id;
	private String productId;
	private String productName;
	private Integer stock;
	private String type;
	private Double price;
	private String image;
	private Date date;
	private Integer quantity;

	public Product(Integer id, String productId, String productName, String type, Integer stock, Double price, String image,
			Date date) {
		this.id = id;
		this.productId = productId;
		this.productName = productName;
		this.stock = stock;
		this.type = type;
		this.price = price;
		this.image = image;
		this.date = date;
	}
	public Product(Integer id, String productId, String productName, Integer quantity, String type, Double price, String image,
			Date date) {
		this.id = id;
		this.productId = productId;
		this.productName = productName;
		this.quantity = quantity;
		this.type = type;
		this.price = price;
		this.image = image;
		this.date = date;
	}
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
