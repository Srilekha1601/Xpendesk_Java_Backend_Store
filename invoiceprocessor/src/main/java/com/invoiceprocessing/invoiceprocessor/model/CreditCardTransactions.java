package com.invoiceprocessing.invoiceprocessor.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CREDIT_CARD_TRANSACTIONS")
public class CreditCardTransactions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cc_transaction_id")
	private Integer ccTransactionId;

	@Column(name = "transaction_no", length = 25)
	private String transactionNo;

	@ManyToOne
	@JoinColumn(name = "employee_id", referencedColumnName = "EMPLOYEE_ID")
	private Employee employee;

	@Column(name = "merchant_name", length = 255)
	private String merchantName;

	@Column(name = "transaction_datetime")
	private Timestamp transactionDateTime;

	@Column(name = "transaction_amount", precision = 15, scale = 2)
	private BigDecimal transactionAmount;

	@Column(name = "expense_type", length = 1)
	private String expenseType;

	@Column(name = "transaction_status", length = 1)
	private String transactionStatus;

}
