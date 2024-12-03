package com.invoiceprocessing.invoiceprocessor.response;

import java.util.List;

public class HistoryOfConsolidatedData {

	private String fromDate;
	private String toDate;
	private String amount;
	private String tripID;
	private String tripName;
	private String expenseStatus;
	private List<String> projectCodeList;
	private List<String> costCodeList;

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

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTripID() {
		return tripID;
	}

	public void setTripID(String tripID) {
		this.tripID = tripID;
	}

	public String getTripName() {
		return tripName;
	}

	public void setTripName(String tripName) {
		this.tripName = tripName;
	}

	public String getExpenseStatus() {
		return expenseStatus;
	}

	public void setExpenseStatus(String expenseStatus) {
		this.expenseStatus = expenseStatus;
	}

	public List<String> getProjectCodeList() {
		return projectCodeList;
	}

	public void setProjectCodeList(List<String> projectCodeList) {
		this.projectCodeList = projectCodeList;
	}

	public List<String> getCostCodeList() {
		return costCodeList;
	}

	public void setCostCodeList(List<String> costCodeList) {
		this.costCodeList = costCodeList;
	}

}
