package com.invoiceprocessing.invoiceprocessor.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ConsolidatedBreakageSummaryDto {

	private Integer tripID;
	private Integer tripConsolidationId;
	private String category;
	private String consolidatedDate;
	private String date;
	private String grade;
	private String partApproval;
	private String containsLiquor;
	private String foodVoucherId;
	private String hotelVoucherId;
	private String objectionRaise;
	private String conveyanceVoucherId;
	private String location;
	private BigDecimal amount;
	private BigDecimal eligibility;
	private String voucherID;
	private String isSingleExpense;
	private boolean isEligible;
	private BigDecimal approvedAmount;
	private BigDecimal rejectedAmount;
	private String isFinalSubmit;
	private String exceptionReason;
	private BigDecimal claimedAmount;
	@JsonIgnore
	private Integer monthlyCap;

	public Integer getTripID() {
		return tripID;
	}

	public void setTripID(Integer tripID) {
		this.tripID = tripID;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getConsolidatedDate() {
		return consolidatedDate;
	}

	public void setConsolidatedDate(String consolidatedDate) {
		this.consolidatedDate = consolidatedDate;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public boolean isEligible() {
		return isEligible;
	}

	public void setEligible(boolean isEligible) {
		this.isEligible = isEligible;
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

	public Integer getMonthlyCap() {
		return monthlyCap;
	}

	public void setMonthlyCap(Integer monthlyCap) {
		this.monthlyCap = monthlyCap;
	}

	public BigDecimal getEligibility() {
		return eligibility;
	}

	public void setEligibility(BigDecimal eligibility) {
		this.eligibility = eligibility;
	}

	public String getVoucherID() {
		return voucherID;
	}

	public void setVoucherID(String voucherID) {
		this.voucherID = voucherID;
	}

	public String getIsSingleExpense() {
		return isSingleExpense;
	}

	public void setIsSingleExpense(String isSingleExpense) {
		this.isSingleExpense = isSingleExpense;
	}

	public Integer getTripConsolidationId() {
		return tripConsolidationId;
	}

	public void setTripConsolidationId(Integer tripConsolidationId) {
		this.tripConsolidationId = tripConsolidationId;
	}

	public String getIsFinalSubmit() {
		return isFinalSubmit;
	}

	public void setIsFinalSubmit(String isFinalSubmit) {
		this.isFinalSubmit = isFinalSubmit;
	}

	public String getPartApproval() {
		return partApproval;
	}

	public void setPartApproval(String partApproval) {
		this.partApproval = partApproval;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContainsLiquor() {
		return containsLiquor;
	}

	public void setContainsLiquor(String containsLiquor) {
		this.containsLiquor = containsLiquor;
	}

	public String getFoodVoucherId() {
		return foodVoucherId;
	}

	public void setFoodVoucherId(String foodVoucherId) {
		this.foodVoucherId = foodVoucherId;
	}

	public String getHotelVoucherId() {
		return hotelVoucherId;
	}

	public void setHotelVoucherId(String hotelVoucherId) {
		this.hotelVoucherId = hotelVoucherId;
	}

	public String getConveyanceVoucherId() {
		return conveyanceVoucherId;
	}

	public void setConveyanceVoucherId(String conveyanceVoucherId) {
		this.conveyanceVoucherId = conveyanceVoucherId;
	}

	public String getObjectionRaise() {
		return objectionRaise;
	}

	public void setObjectionRaise(String objectionRaise) {
		this.objectionRaise = objectionRaise;
	}

	public BigDecimal getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(BigDecimal approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public BigDecimal getRejectedAmount() {
		return rejectedAmount;
	}

	public void setRejectedAmount(BigDecimal rejectedAmount) {
		this.rejectedAmount = rejectedAmount;
	}
	
	@Override
	public String toString() {
	    return "ConsolidatedBreakageSummaryDto{" +
	           "tripID=" + tripID +
	           ", tripConsolidationId=" + tripConsolidationId +
	           ", category='" + category + '\'' +
	           ", consolidatedDate='" + consolidatedDate + '\'' +
	           ", date='" + date + '\'' +
	           ", grade='" + grade + '\'' +
	           ", partApproval='" + partApproval + '\'' +
	           ", containsLiquor='" + containsLiquor + '\'' +
	           ", foodVoucherId='" + foodVoucherId + '\'' +
	           ", hotelVoucherId='" + hotelVoucherId + '\'' +
	           ", objectionRaise='" + objectionRaise + '\'' +
	           ", conveyanceVoucherId='" + conveyanceVoucherId + '\'' +
	           ", location='" + location + '\'' +
	           ", amount=" + amount +
	           ", eligibility=" + eligibility +
	           ", voucherID='" + voucherID + '\'' +
	           ", isSingleExpense='" + isSingleExpense + '\'' +
	           ", isEligible=" + isEligible +
	           ", approvedAmount=" + approvedAmount +
	           ", rejectedAmount=" + rejectedAmount +
	           ", isFinalSubmit='" + isFinalSubmit + '\'' +
	           ", exceptionReason='" + exceptionReason + '\'' +
	           ", claimedAmount=" + claimedAmount +
	           '}';
	}


}
