package com.invoiceprocessing.invoiceprocessor.response;

import lombok.Data;

@Data
public class PendingTaskDto {

	private Integer employeeId;

	private String tripName;

	private String submittedBy;

	private String gradeId;

	private String expenseType;

	private String date;

	private String referenceType;

	private String referenceId;

	private String amount;

	private String taskId;

	private String stepSequence;

	private String exceptionReasonExists;

	private String liquorStatus;
	
	private String approvedAmount;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer additionalAssignee;
	
	private String additionalAssigneeName;

}
