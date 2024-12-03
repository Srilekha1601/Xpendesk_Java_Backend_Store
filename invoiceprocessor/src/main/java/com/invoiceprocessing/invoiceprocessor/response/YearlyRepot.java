package com.invoiceprocessing.invoiceprocessor.response;

import java.math.BigDecimal;

public class YearlyRepot {

	private String monthsName;
	private BigDecimal amount;

	public String getMonthsName() {
		return monthsName;
	}

	public void setMonthsName(String monthsName) {
		this.monthsName = monthsName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
