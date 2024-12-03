package com.invoiceprocessing.invoiceprocessor.response;

import java.sql.Date;

import lombok.Data;

@Data
public class CostCodeDto {

	private Integer costId;

	private String costCode;

	private String costDescription;

	private String isEnable;

	private Date validFrom;

	private Date validTo;

	private String amount;
	
}
