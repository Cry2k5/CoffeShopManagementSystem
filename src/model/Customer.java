package model;

import java.sql.Date;

public class Customer {
	
	private Integer id;
	private Integer customerID;
	private Double total;
	private Date date;
	private String employee_email;
	
	
	public Customer(Integer id, Integer customerID, Double total, Date date, String employee_email) {
		this.id = id;
		this.customerID = customerID;
		this.total = total;
		this.date = date;
		this.employee_email = employee_email;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getCustomerID() {
		return customerID;
	}


	public void setCustomerID(Integer customerID) {
		this.customerID = customerID;
	}


	public Double getTotal() {
		return total;
	}


	public void setTotal(Double total) {
		this.total = total;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getEmployee_email() {
		return employee_email;
	}


	public void setEmployee_email(String employee_email) {
		this.employee_email = employee_email;
	}
	
	
	
}
