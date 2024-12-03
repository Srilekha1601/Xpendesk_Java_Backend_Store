package com.invoiceprocessing.invoiceprocessor.response;

import java.util.List;

import lombok.Data;

@Data
public class LoginResponseDto {

	private String token;

	private String userName;

	private String employeeName;

	private Integer employeeId;

	private List<String> listOfRole;

}
