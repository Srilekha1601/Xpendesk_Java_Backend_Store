package com.invoiceprocessing.invoiceprocessor.response;

import java.util.List;

import lombok.Data;

@Data
public class ProjectDetailsInfoDto {

	private List<String> projectCode;

	private List<String> amount;

}
