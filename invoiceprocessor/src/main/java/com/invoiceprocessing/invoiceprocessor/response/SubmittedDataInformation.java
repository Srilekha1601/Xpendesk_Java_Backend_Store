package com.invoiceprocessing.invoiceprocessor.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SubmittedDataInformation {

	private Integer employeeID;
	private Integer totalFoodInvoices;
	@JsonIgnore
	private String invoiceType;
	private Integer totalHotelInvoices;
	private Integer totalConveyanceInvoices;

	public Integer getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(Integer employeeID) {
		this.employeeID = employeeID;
	}

	public Integer getTotalFoodInvoices() {
		return totalFoodInvoices;
	}

	public void setTotalFoodInvoices(Integer totalFoodInvoices) {
		this.totalFoodInvoices = totalFoodInvoices;
	}

	public Integer getTotalHotelInvoices() {
		return totalHotelInvoices;
	}

	public void setTotalHotelInvoices(Integer totalHotelInvoices) {
		this.totalHotelInvoices = totalHotelInvoices;
	}

	public Integer getTotalConveyanceInvoices() {
		return totalConveyanceInvoices;
	}

	public void setTotalConveyanceInvoices(Integer totalConveyanceInvoices) {
		this.totalConveyanceInvoices = totalConveyanceInvoices;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

}
