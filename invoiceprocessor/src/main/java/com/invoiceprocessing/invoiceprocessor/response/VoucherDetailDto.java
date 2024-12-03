package com.invoiceprocessing.invoiceprocessor.response;

public class VoucherDetailDto {

	private String description;
	private String amount;
	private String sacCode;
	private String date;
	private String serviceType;
	private int id;
	private String gst1;
	private String gst2;
	private String exceptionReason;
	private Integer dateFormat;
	private Integer amountFormat;
	private Integer gst1Format;
	private Integer gst2Format;
	private String detailVoucherID;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSacCode() {
		return sacCode;
	}

	public void setSacCode(String sacCode) {
		this.sacCode = sacCode;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGst1() {
		return gst1;
	}

	public void setGst1(String gst1) {
		this.gst1 = gst1;
	}

	public String getGst2() {
		return gst2;
	}

	public void setGst2(String gst2) {
		this.gst2 = gst2;
	}

	public String getExceptionReason() {
		return exceptionReason;
	}

	public void setExceptionReason(String exceptionReason) {
		this.exceptionReason = exceptionReason;
	}

	public Integer getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(Integer dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Integer getAmountFormat() {
		return amountFormat;
	}

	public void setAmountFormat(Integer amountFormat) {
		this.amountFormat = amountFormat;
	}

	public Integer getGst1Format() {
		return gst1Format;
	}

	public void setGst1Format(Integer gst1Format) {
		this.gst1Format = gst1Format;
	}

	public Integer getGst2Format() {
		return gst2Format;
	}

	public void setGst2Format(Integer gst2Format) {
		this.gst2Format = gst2Format;
	}

	public String getDetailVoucherID() {
		return detailVoucherID;
	}

	public void setDetailVoucherID(String detailVoucherID) {
		this.detailVoucherID = detailVoucherID;
	}

}
