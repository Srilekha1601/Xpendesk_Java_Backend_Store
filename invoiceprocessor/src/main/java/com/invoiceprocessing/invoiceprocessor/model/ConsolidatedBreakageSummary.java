package com.invoiceprocessing.invoiceprocessor.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class ConsolidatedBreakageSummary extends AuditFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRIP_CONSOLIDATION_ID")
	private Integer tripConsolidationID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TRIP_ID")
	private Trip trip;

	@Column(name = "CATEGORY")
	private String category;

	@Column(name = "CONSOLIDATION_DATE")
	private String consolidationDate;

	@Column(name = "AMOUNT")
	private BigDecimal amount;

	@Column(name = "CLAIMED_AMOUNT")
	private BigDecimal claimedAmount;

	@Column(name = "PART_APPROVAL", length = 1)
	private String partApproval;

	@Column(name = "APPROVED_AMOUNT", precision = 32, scale = 2)
	private BigDecimal approvedAmount;

	@Column(name = "REJECTED_AMOUNT", precision = 32, scale = 2)
	private BigDecimal rejectAmount;

	@Column(name = "EXCEPTION_REASON")
	private String exceptionReason;

	@OneToMany(mappedBy = "consolidatedBreakageSummary", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ConsolidatedExpenseDetail> listOfConsolidatedExpenseDetail;

	public Integer getTripConsolidationID() {
		return tripConsolidationID;
	}

	public void setTripConsolidationID(Integer tripConsolidationID) {
		this.tripConsolidationID = tripConsolidationID;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getConsolidationDate() {
		return consolidationDate;
	}

	public void setConsolidationDate(String consolidationDate) {
		this.consolidationDate = consolidationDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(BigDecimal claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public String getExceptionReason() {
		return exceptionReason;
	}

	public void setExceptionReason(String exceptionReason) {
		this.exceptionReason = exceptionReason;
	}

	public String getPartApproval() {
		return partApproval;
	}

	public void setPartApproval(String partApproval) {
		this.partApproval = partApproval;
	}

	public BigDecimal getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(BigDecimal approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public BigDecimal getRejectAmount() {
		return rejectAmount;
	}

	public void setRejectAmount(BigDecimal rejectAmount) {
		this.rejectAmount = rejectAmount;
	}

}
