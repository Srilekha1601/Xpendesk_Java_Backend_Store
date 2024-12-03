package com.invoiceprocessing.invoiceprocessor.response;

import lombok.Data;

@Data
public class BarChartReportUserExpenseDto {

	private Integer employeeId;

	private String employeeName;

	private BarChartInfoDto barChartInfoDto;

}
