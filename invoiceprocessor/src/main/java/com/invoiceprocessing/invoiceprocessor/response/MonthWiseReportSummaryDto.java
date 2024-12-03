package com.invoiceprocessing.invoiceprocessor.response;

public class MonthWiseReportSummaryDto {

	private String month;
	private Integer foodExpense;
	private Integer hotelExpense;
	private Integer conveyanceExpense;

	public MonthWiseReportSummaryDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MonthWiseReportSummaryDto(String month, Integer foodExpense, Integer hotelExpense,
			Integer conveyanceExpense) {
		super();
		this.month = month;
		this.foodExpense = foodExpense;
		this.hotelExpense = hotelExpense;
		this.conveyanceExpense = conveyanceExpense;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getFoodExpense() {
		return foodExpense == null ? 0 : foodExpense;
	}

	public void setFoodExpense(Integer foodExpense) {
		this.foodExpense = foodExpense;
	}

	public Integer getHotelExpense() {
		return hotelExpense == null ? 0 : hotelExpense;
	}

	public void setHotelExpense(Integer hotelExpense) {
		this.hotelExpense = hotelExpense;
	}

	public Integer getConveyanceExpense() {
		return conveyanceExpense == null ? 0 : conveyanceExpense;
	}

	public void setConveyanceExpense(Integer conveyanceExpense) {
		this.conveyanceExpense = conveyanceExpense;
	}

}
