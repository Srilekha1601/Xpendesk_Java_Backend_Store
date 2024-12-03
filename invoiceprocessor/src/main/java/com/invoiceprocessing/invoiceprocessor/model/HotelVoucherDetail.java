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

@Entity
@Table(name = "HOTEL_VOUCHER_DETAIL")
public class HotelVoucherDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "HOTEL_VOUCHER_DETAIL_ID")
	private Integer hotelVoucherDetailId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "HOTEL_VOUCHER_ID")
	private HotelVoucher hotelVoucherID;

	@Column(name = "ITEM_DATE")
	private Date date;

	@Column(name = "DESCRIPTION", length = 100)
	private String description;

	@Column(name = "BILL_TYPE", length = 20)
	private String billType;

	@Column(name = "SAC_CODE", length = 20)
	private String sacCode;

	@Column(name = "AMOUNT", precision = 15, scale = 2)
	private BigDecimal amount;

	@Column(name = "GST_AMOUNT", precision = 15, scale = 2)
	private BigDecimal gstAmount;

	@Column(name = "CLAIMED_AMOUNT", precision = 15, scale = 2)
	private BigDecimal claimedAmount;

	@Column(name = "EXCEPTION_REASON")
	private String exceptionReason;

	@Column(name = "CGST_AMOUNT", precision = 15, scale = 2)
	private BigDecimal cgstAmount;

	@Column(name = "SGST_AMOUNT", precision = 15, scale = 2)
	private BigDecimal sgstAmount;

	public Integer getHotelVoucherDetailId() {
		return hotelVoucherDetailId;
	}

	public void setHotelVoucherDetailId(Integer hotelVoucherDetailId) {
		this.hotelVoucherDetailId = hotelVoucherDetailId;
	}

	public HotelVoucher getHotelVoucher() {
		return hotelVoucherID;
	}

	public void setHotelVoucher(HotelVoucher hotelVoucherID) {
		this.hotelVoucherID = hotelVoucherID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getSacCode() {
		return sacCode;
	}

	public void setSacCode(String sacCode) {
		this.sacCode = sacCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getGstAmount() {
		return gstAmount;
	}

	public void setGstAmount(BigDecimal gstAmount) {
		this.gstAmount = gstAmount;
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

	public BigDecimal getCgstAmount() {
		return cgstAmount;
	}

	public void setCgstAmount(BigDecimal cgstAmount) {
		this.cgstAmount = cgstAmount;
	}

	public BigDecimal getSgstAmount() {
		return sgstAmount;
	}

	public void setSgstAmount(BigDecimal sgstAmount) {
		this.sgstAmount = sgstAmount;
	}

}
