package com.invoiceprocessing.invoiceprocessor.response;

import lombok.Data;

@Data
public class NotificationDto {

	private Integer employeeId;

	private String tripName;

	private String submittedBy;

	private String grade;

	private String expenseType;

	private String referenceType;

	private String referenceId;

	private String amount;

	private String taskId;

	private String stepSequence;

	private String comments;

	private String taskDescription;

	private String actionType;

}
