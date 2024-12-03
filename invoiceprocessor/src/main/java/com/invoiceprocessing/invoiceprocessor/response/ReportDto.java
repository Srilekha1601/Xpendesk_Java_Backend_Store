package com.invoiceprocessing.invoiceprocessor.response;

import java.util.List;

public class ReportDto {

	private String fromDate;
	private String toDate;
	private String tripID;
	private String amount;
	private String tripName;
	private List<DateWiseAmountBreakdownDto> listOfDateWiseAmountBreakdown;

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

	public String getTripID() {
		return tripID;
	}

	public void setTripID(String tripID) {
		this.tripID = tripID;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public List<DateWiseAmountBreakdownDto> getListOfDateWiseAmountBreakdown() {
		return listOfDateWiseAmountBreakdown;
	}

	public void setListOfDateWiseAmountBreakdown(List<DateWiseAmountBreakdownDto> listOfDateWiseAmountBreakdown) {
		this.listOfDateWiseAmountBreakdown = listOfDateWiseAmountBreakdown;
	}

	public String getTripName() {
		return tripName;
	}

	public void setTripName(String tripName) {
		this.tripName = tripName;
	}

}
