package com.invoiceprocessing.invoiceprocessor.response;

import java.util.List;

import lombok.Data;

@Data
public class RoleWiseReportDto {

	private Integer employeeId;

	private String employeeName;

	private String employeeCode;

	private String employeeGrade;

	private List<ExpenseReportDto> employeeExpenses;

}
