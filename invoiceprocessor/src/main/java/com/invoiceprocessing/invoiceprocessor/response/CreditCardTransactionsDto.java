package com.invoiceprocessing.invoiceprocessor.response;

import lombok.Data;

@Data
public class CreditCardTransactionsDto {

	private String ccTransactionId;

	private String transactionNo;

	private String merchantName;

	private String employeeId;

	private String transactionDateTime;

	private String transactionAmount;

	private String expenseType;

	private String transactionStatus;

	private String voucherId;

}
