package com.invoiceprocessing.invoiceprocessor.model;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "CONSOLIDATED_EXPENSE")
public class ConsolidatedExpense extends AuditFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CONSOLIDATED_EXPENSE_ID")
	private Integer consolidatedExpenseID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TRIP_ID")
	private Trip trip;

	@Column(name = "EXPENSE_DATE", nullable = false)
	private Date expenseDate;

	@Column(name = "EXPENSE_TYPE", length = 50, nullable = false)
	private String expenseType;

	@Column(name = "BILL_AMOUNT", precision = 15, scale = 2)
	private BigDecimal billAmount;

	@Column(name = "ALLOWANCE_AMOUNT", precision = 15, scale = 2)
	private BigDecimal allowanceAmount;

	@Column(name = "CLAIMED_AMOUNT", precision = 15, scale = 2)
	private BigDecimal claimedAmount;

	@Column(name = "EXCEPTION_REASON", length = 300)
	private String exceptionReason;

}
