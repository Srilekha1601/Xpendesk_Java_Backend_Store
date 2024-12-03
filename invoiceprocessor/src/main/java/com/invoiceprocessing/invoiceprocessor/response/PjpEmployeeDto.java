package com.invoiceprocessing.invoiceprocessor.response;

import java.util.List;

import lombok.Data;

@Data
public class PjpEmployeeDto {

	private Integer employeeId;

	private Integer month;

	private Integer year;

	private List<PjpDto> listOfPjpDto;

}
