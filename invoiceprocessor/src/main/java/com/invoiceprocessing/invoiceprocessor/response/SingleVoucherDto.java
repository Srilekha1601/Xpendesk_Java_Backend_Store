package com.invoiceprocessing.invoiceprocessor.response;

import java.util.List;

public class SingleVoucherDto {

	private String voucherDate;
	private String voucherID;
	private String billType;
	private String amount;
	private String expenseStatus;
	private List<String> listOfProjects;
	private List<String> listOfCosts;

	public String getVoucherDate() {
		return voucherDate;
	}

	public void setVoucherDate(String voucherDate) {
		this.voucherDate = voucherDate;
	}

	public String getVoucherID() {
		return voucherID;
	}

	public void setVoucherID(String voucherID) {
		this.voucherID = voucherID;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public List<String> getListOfProjects() {
		return listOfProjects;
	}

	public void setListOfProjects(List<String> listOfProjects) {
		this.listOfProjects = listOfProjects;
	}

	public List<String> getListOfCosts() {
		return listOfCosts;
	}

	public void setListOfCosts(List<String> listOfCosts) {
		this.listOfCosts = listOfCosts;
	}

	public String getExpenseStatus() {
		return expenseStatus;
	}

	public void setExpenseStatus(String expenseStatus) {
		this.expenseStatus = expenseStatus;
	}

}
