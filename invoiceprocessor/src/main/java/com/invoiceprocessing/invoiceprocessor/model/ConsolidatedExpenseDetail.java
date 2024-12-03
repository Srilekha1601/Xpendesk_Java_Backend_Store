package com.invoiceprocessing.invoiceprocessor.model;

import com.invoiceprocessing.invoiceprocessor.service.XpendeskEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consolidated_expense_detail")
@EntityListeners(XpendeskEntityListener.class)
public class ConsolidatedExpenseDetail extends AuditFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CONSOLIDATED_EXPENSE_DETAIL_ID")
	private Integer consolidatedExpenseDetailId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TRIP_CONSOLIDATION_ID")
	private ConsolidatedBreakageSummary consolidatedBreakageSummary;

	@Column(name = "VOUCHER_IDS", length = 255)
	private String voucherIds;

	@Column(name = "APPROVED_VOUCHER_IDS", length = 255)
	private String approvedVoucherIds;

	@Column(name = "REJECTED_VOUCHER_IDS", length = 255)
	private String rejectedVoucherIds;

}