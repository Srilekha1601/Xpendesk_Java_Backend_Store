package com.invoiceprocessing.invoiceprocessor.response;

import java.sql.Date;

import lombok.Data;

@Data
public class ProjectDto {

	private Integer projectId;

	private String projectCode;

	private String projectDescription;

	private String isActive;

	private Date startDate;

	private Date endDate;

	private String amount;

}
