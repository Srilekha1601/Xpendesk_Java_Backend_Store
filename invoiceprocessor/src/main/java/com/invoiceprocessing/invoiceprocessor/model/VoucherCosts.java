package com.invoiceprocessing.invoiceprocessor.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "VOUCHER_COSTS")
public class VoucherCosts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "VOUCHER_COST_ID")
	private Integer voucherCostId;

	@ManyToOne
	@JoinColumn(name = "VOUCHER_ID", referencedColumnName = "VOUCHER_ID")
	private Voucher voucher;

	@ManyToOne
	@JoinColumn(name = "COST_ID", referencedColumnName = "cost_id")
	private CostCode costCode;

	@Column(name = "CLAIMED_AMOUNT", precision = 15, scale = 2)
	private BigDecimal claimedAmount;

}
