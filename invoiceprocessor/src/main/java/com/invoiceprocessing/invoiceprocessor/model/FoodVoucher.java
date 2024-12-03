package com.invoiceprocessing.invoiceprocessor.model;

import java.math.BigDecimal;
import java.util.List;

import com.invoiceprocessing.invoiceprocessor.service.XpendeskEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "FOOD_VOUCHER")
@EntityListeners(XpendeskEntityListener.class)
public class FoodVoucher extends AuditFields implements VoucherEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FOOD_VOUCHER_ID")
	private Integer foodVoucherID;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "VOUCHER_ID")
	private Voucher voucherID;

	@Column(name = "TOTAL_AMOUNT", precision = 15, scale = 2)
	private BigDecimal totalAmount;

	@Column(name = "GST_AMOUNT", precision = 15, scale = 2)
	private BigDecimal gstAmount;

	@Column(name = "CONTAINS_LIQUOR")
	private Boolean containsLiquor;

	@Column(name = "CLAIMED_AMOUNT", precision = 15, scale = 2)
	private BigDecimal claimedAmount;

	@Column(name = "EXCEPTION_REASON", length = 100)
	private String exceptionReason;

	@OneToMany(mappedBy = "foodVoucherID", fetch = FetchType.LAZY)
	private List<FoodVoucherDetail> listOfFoodVoucherDetails;

	@Column(name = "CGST_AMOUNT", precision = 15, scale = 2)
	private String cgstAmount;

	@Column(name = "SGST_AMOUNT", precision = 15, scale = 2)
	private String sgstAmount;

	@Column(name = "OBJECTION_RAISE", length = 300)
	private String objectionRaise;

	public Integer getFoodVoucherID() {
		return foodVoucherID;
	}

	public void setFoodVoucherID(Integer foodVoucherID) {
		this.foodVoucherID = foodVoucherID;
	}

	public Voucher getVoucherID() {
		return voucherID;
	}

	public void setVoucherID(Voucher voucherID) {
		this.voucherID = voucherID;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getGstAmount() {
		return gstAmount;
	}

	public void setGstAmount(BigDecimal gstAmount) {
		this.gstAmount = gstAmount;
	}

	public Boolean getContainsLiquor() {
		return containsLiquor;
	}

	public void setContainsLiquor(Boolean containsLiquor) {
		this.containsLiquor = containsLiquor;
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

	public List<FoodVoucherDetail> getListOfFoodVoucherDetails() {
		return listOfFoodVoucherDetails;
	}

	public void setListOfFoodVoucherDetails(List<FoodVoucherDetail> listOfFoodVoucherDetails) {
		this.listOfFoodVoucherDetails = listOfFoodVoucherDetails;
	}

	public String getCgstAmount() {
		return cgstAmount;
	}

	public void setCgstAmount(String cgstAmount) {
		this.cgstAmount = cgstAmount;
	}

	public String getSgstAmount() {
		return sgstAmount;
	}

	public void setSgstAmount(String sgstAmount) {
		this.sgstAmount = sgstAmount;
	}

	public String getObjectionRaise() {
		return objectionRaise;
	}

	public void setObjectionRaise(String objectionRaise) {
		this.objectionRaise = objectionRaise;
	}

}
