package com.invoiceprocessing.invoiceprocessor.response;

import lombok.Data;

@Data
public class AssignUserInfoDto {

	public Integer employeeId;

	public String employeeCode;

	public String employeeName;

	public String employeeGrade;

	public String employeeDesignation;

	public String userName;

}
