package com.invoiceprocessing.invoiceprocessor.response;

public class DateWiseAmountBreakdownDto {

	private String tripID;
	private String date;
	private String foodAmount;
	private String accommodationAmount;
	private String conveyanceAmount;
	private String outhersAmount;

	public String getTripID() {
		return tripID;
	}

	public void setTripID(String tripID) {
		this.tripID = tripID;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFoodAmount() {
		return foodAmount;
	}

	public void setFoodAmount(String foodAmount) {
		this.foodAmount = foodAmount;
	}

	public String getAccommodationAmount() {
		return accommodationAmount;
	}

	public void setAccommodationAmount(String accommodationAmount) {
		this.accommodationAmount = accommodationAmount;
	}

	public String getConveyanceAmount() {
		return conveyanceAmount;
	}

	public void setConveyanceAmount(String conveyanceAmount) {
		this.conveyanceAmount = conveyanceAmount;
	}

	public String getOuthersAmount() {
		return outhersAmount;
	}

	public void setOuthersAmount(String outhersAmount) {
		this.outhersAmount = outhersAmount;
	}

}
