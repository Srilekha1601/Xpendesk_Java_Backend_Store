package com.invoiceprocessing.invoiceprocessor.response;

import java.util.List;

public class TripDTO {

	private String fromDate;
	private String toDate;
	private String employeeID;
	private String expenseStatus;
	private String tripName;
	private List<String> projectCodes;
	private List<String> costCodes;

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getExpenseStatus() {
		return expenseStatus;
	}

	public void setExpenseStatus(String expenseStatus) {
		this.expenseStatus = expenseStatus;
	}

	public String getTripName() {
		return tripName;
	}

	public void setTripName(String tripName) {
		this.tripName = tripName;
	}

	public List<String> getProjectCodes() {
		return projectCodes;
	}

	public void setProjectCodes(List<String> projectCodes) {
		this.projectCodes = projectCodes;
	}

	public List<String> getCostCodes() {
		return costCodes;
	}

	public void setCostCodes(List<String> costCodes) {
		this.costCodes = costCodes;
	}

}
