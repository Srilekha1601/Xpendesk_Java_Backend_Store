package com.invoiceprocessing.invoiceprocessor.response;

import java.util.List;

import lombok.Data;

@Data
public class CostDetailsInfoDto {

	private List<String> costCode;

	private List<String> amount;

}
