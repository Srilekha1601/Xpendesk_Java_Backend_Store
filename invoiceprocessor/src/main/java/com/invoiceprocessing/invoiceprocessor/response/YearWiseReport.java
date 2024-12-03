package com.invoiceprocessing.invoiceprocessor.response;

import java.util.List;

public class YearWiseReport {

	private Integer employeeID;
	private List<YearlyRepot> monthWiseReport;

	public Integer getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(Integer employeeID) {
		this.employeeID = employeeID;
	}

	public List<YearlyRepot> getMonthWiseReport() {
		return monthWiseReport;
	}

	public void setMonthWiseReport(List<YearlyRepot> monthWiseReport) {
		this.monthWiseReport = monthWiseReport;
	}

}
