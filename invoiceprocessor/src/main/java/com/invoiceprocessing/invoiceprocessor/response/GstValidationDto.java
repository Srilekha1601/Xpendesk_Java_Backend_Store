package com.invoiceprocessing.invoiceprocessor.response;

import lombok.Data;

@Data
public class GstValidationDto {

	private String employeeId;

	private String gstExtracted;

}
