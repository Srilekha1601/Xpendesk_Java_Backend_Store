package com.invoiceprocessing.invoiceprocessor.response;

import java.util.List;

public class MonthWiseExpenseDto {

	private Integer employeeID;
	private List<MonthWiseReportSummaryDto> listOfMonthWiseReportDto;

	public Integer getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(Integer employeeID) {
		this.employeeID = employeeID;
	}

	public List<MonthWiseReportSummaryDto> getListOfMonthWiseReportDto() {
		return listOfMonthWiseReportDto;
	}

	public void setListOfMonthWiseReportDto(List<MonthWiseReportSummaryDto> listOfMonthWiseReportDto) {
		this.listOfMonthWiseReportDto = listOfMonthWiseReportDto;
	}

}
