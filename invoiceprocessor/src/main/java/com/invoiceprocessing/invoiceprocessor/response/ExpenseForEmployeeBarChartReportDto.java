package com.invoiceprocessing.invoiceprocessor.response;

import lombok.Data;

@Data
public class ExpenseForEmployeeBarChartReportDto {

	private Integer employeeId;

	private String employeeName;

	private ExpenseReportDataForBarChartDto expenseReportDataForBarChart;

}
