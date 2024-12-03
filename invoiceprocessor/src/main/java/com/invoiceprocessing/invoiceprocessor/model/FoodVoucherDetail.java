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
@Table(name = "FOOD_VOUCHER_DETAIL")
public class FoodVoucherDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FOOD_VOUCHER_DETAIL_ID")
	private Integer foodVoucherDetailID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FOOD_VOUCHER_ID")
	private FoodVoucher foodVoucherID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "HOTEL_VOUCHER_ID")
	private HotelVoucher hotelVoucher;

	@Column(name = "DESCRIPTION", length = 100)
	private String description;

	@Column(name = "BILL_TYPE", length = 25)
	private String billType;

	@Column(name = "AMOUNT", precision = 15, scale = 2)
	private BigDecimal amount;

	@Column(name = "GST_AMOUNT", precision = 15, scale = 2)
	private BigDecimal gstAmount;

	@Column(name = "ITEM_DATE")
	private Date itemDate;

	@Column(name = "CGST_AMOUNT", precision = 15, scale = 2)
	private BigDecimal cgstAmount;

	@Column(name = "SGST_AMOUNT", precision = 15, scale = 2)
	private BigDecimal sgstAmount;

	public Integer getFoodVoucherDetailID() {
		return foodVoucherDetailID;
	}

	public void setFoodVoucherDetailID(Integer foodVoucherDetailID) {
		this.foodVoucherDetailID = foodVoucherDetailID;
	}

	public FoodVoucher getFoodVoucherID() {
		return foodVoucherID;
	}

	public void setFoodVoucherID(FoodVoucher foodVoucherID) {
		this.foodVoucherID = foodVoucherID;
	}

	public HotelVoucher getHotelVoucher() {
		return hotelVoucher;
	}

	public void setHotelVoucher(HotelVoucher hotelVoucher) {
		this.hotelVoucher = hotelVoucher;
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

	public Date getItemDate() {
		return itemDate;
	}

	public void setItemDate(Date itemDate) {
		this.itemDate = itemDate;
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
