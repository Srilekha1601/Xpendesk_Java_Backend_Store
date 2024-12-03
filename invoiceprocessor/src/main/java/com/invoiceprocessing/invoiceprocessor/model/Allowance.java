package com.invoiceprocessing.invoiceprocessor.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ALLOWANCE")
public class Allowance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ELIGIBILITY_ID")
	private Integer eligibilityID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GRADE_ID")
	private Grade gradeID;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TIER_ID")
	private Tier tierID;

	@Column(name = "INVOICE_TYPE", length = 10, nullable = false)
	private String invoiceType;

	@Column(name = "PERIOD", nullable = false, length = 10)
	private String period;

	@Column(name = "AMOUNT", nullable = false, precision = 10, scale = 2)
	private BigDecimal amount;

	@Column(name = "TRAVEL_MODE", length = 10)
	private String travelMode;

	@Column(name = "TRAVEL_CLASS", length = 10)
	private String travelClass;

	public Integer getEligibilityID() {
		return eligibilityID;
	}

	public void setEligibilityID(Integer eligibilityID) {
		this.eligibilityID = eligibilityID;
	}

	public Grade getGradeID() {
		return gradeID;
	}

	public void setGradeID(Grade gradeID) {
		this.gradeID = gradeID;
	}

	public Tier getTierID() {
		return tierID;
	}

	public void setTierID(Tier tierID) {
		this.tierID = tierID;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTravelMode() {
		return travelMode;
	}

	public void setTravelMode(String travelMode) {
		this.travelMode = travelMode;
	}

	public String getTravelClass() {
		return travelClass;
	}

	public void setTravelClass(String travelClass) {
		this.travelClass = travelClass;
	}

}
