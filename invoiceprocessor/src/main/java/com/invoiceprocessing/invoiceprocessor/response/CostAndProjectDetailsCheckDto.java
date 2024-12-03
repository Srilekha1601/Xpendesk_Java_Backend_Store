package com.invoiceprocessing.invoiceprocessor.response;

import lombok.Data;

@Data
public class CostAndProjectDetailsCheckDto {

	private ProjectDetailsInfoDto projectDetailsInfoDtos;

	private CostDetailsInfoDto costDetailsInfoDtos;

}
