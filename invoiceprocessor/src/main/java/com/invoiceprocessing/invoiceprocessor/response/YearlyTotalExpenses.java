package com.invoiceprocessing.invoiceprocessor.response;

import java.math.BigDecimal;

public class YearlyTotalExpenses {

	private String employeeID;
	private String year;
	private BigDecimal amount;

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
