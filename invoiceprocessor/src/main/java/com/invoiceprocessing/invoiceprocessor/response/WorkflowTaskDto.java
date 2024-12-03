package com.invoiceprocessing.invoiceprocessor.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class WorkflowTaskDto {

	private Integer taskID;

	private String taskDescription;

	private Integer referenceID;

	private String referenceType;

	private Integer stepSequence;

	private Integer generatedBy;

	private String generatedOn;

	private Integer assignedTo;

	private Integer executedBy;

	private String executedOn;

	private String actionType;

	private String comments;

	private List<WorkflowApprovalAmountDto> considWiseApproveAmmount;

	private BigDecimal approvedAmount;

}
