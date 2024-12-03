package com.invoiceprocessing.invoiceprocessor.model;

import java.math.BigDecimal;
import java.sql.Date;

import com.invoiceprocessing.invoiceprocessor.service.XpendeskEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "VOUCHER")
@EntityListeners(XpendeskEntityListener.class)
public class Voucher extends AuditFields implements VoucherEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "VOUCHER_ID")
	private Integer voucherID;

	@Column(name = "VOUCHER_DATE", nullable = false)
	private Date voucherDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TRIP_ID")
	private Trip trip;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EMPLOYEE_ID")
	private Employee employeeID;

	@Column(name = "FILE_NAME", length = 100)
	private String fileName;

	@Column(name = "MERCHANT_NAME", length = 200)
	private String merchantName;

	@Column(name = "INVOICE_NO", length = 50)
	private String invoiceNumber;

	@Column(name = "INVOICE_DATE")
	private Date invoiceDate;

	@Column(name = "INVOICE_TYPE", length = 20, nullable = false)
	private String invoiceType;

	@Column(name = "TOTAL_AMOUNT", precision = 15, scale = 2)
	private BigDecimal totalAmount;

	@Column(name = "APPROVED_AMOUNT", precision = 15, scale = 2)
	private BigDecimal approvedAmount;

	@Column(name = "CLAIMED_AMOUNT", precision = 15, scale = 2)
	private BigDecimal claimedAmount;

	@Column(name = "EXCEPTION_REASON", length = 100)
	private String exceptionReason;

	@Column(name = "HASH_TEXT", length = 100)
	private String hashText;

	@Lob
	@Column(name = "FILE_IMAGE", length = 100000)
	private byte[] fileImage;

	@OneToOne(mappedBy = "voucherID", fetch = FetchType.LAZY)
	private HotelVoucher hotelVoucherID;

	@OneToOne(mappedBy = "voucherID", fetch = FetchType.LAZY)
	private FoodVoucher foodVoucherID;

	@OneToOne(mappedBy = "voucherID", fetch = FetchType.LAZY)
	private ConveyanceVoucher conveyanceVoucherID;

	@ManyToOne
	@JoinColumn(name = "CC_TRANSACTION_ID", referencedColumnName = "cc_transaction_id")
	private CreditCardTransactions creditCardTransactions;

	@Column(name = "MANUAL_ENTRY", length = 3)
	private String manualEntry;

	@Column(name = "TOTAL_PAX")
	private Integer totalPax;

	@Column(name = "SINGLE_EXPENSE", length = 3)
	private String isSingleExpense;

	@Column(name = "expense_status", length = 1)
	private String expenseStatus;

	@Column(name = "PAYMENT_MODE", length = 1)
	private String paymentMode;

	@Column(name = "DELETE_STATUS", length = 1)
	private String deleteStatus;

	@Column(name = "REASON", length = 150)
	private String reason;

	@Column(name = "IS_APPROVED", length = 1)
	private String isApproved;

	public Integer getVoucherID() {
		return voucherID;
	}

	public void setVoucherID(Integer voucherID) {
		this.voucherID = voucherID;
	}

	public Date getVoucherDate() {
		return voucherDate;
	}

	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}

	public Employee getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(Employee employeeID) {
		this.employeeID = employeeID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
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

	public String getHashText() {
		return hashText;
	}

	public void setHashText(String hashText) {
		this.hashText = hashText;
	}

	public byte[] getFileImage() {
		return fileImage;
	}

	public void setFileImage(byte[] fileImage) {
		this.fileImage = fileImage;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public HotelVoucher getHotelVoucherID() {
		return hotelVoucherID;
	}

	public void setHotelVoucherID(HotelVoucher hotelVoucherID) {
		this.hotelVoucherID = hotelVoucherID;
	}

	public FoodVoucher getFoodVoucherID() {
		return foodVoucherID;
	}

	public void setFoodVoucherID(FoodVoucher foodVoucherID) {
		this.foodVoucherID = foodVoucherID;
	}

	public ConveyanceVoucher getConveyanceVoucherID() {
		return conveyanceVoucherID;
	}

	public void setConveyanceVoucherID(ConveyanceVoucher conveyanceVoucherID) {
		this.conveyanceVoucherID = conveyanceVoucherID;
	}

	public String getManualEntry() {
		return manualEntry;
	}

	public void setManualEntry(String manualEntry) {
		this.manualEntry = manualEntry;
	}

	public Integer getTotalPax() {
		return totalPax;
	}

	public void setTotalPax(Integer totalPax) {
		this.totalPax = totalPax;
	}

	public String getIsSingleExpense() {
		return isSingleExpense;
	}

	public void setIsSingleExpense(String isSingleExpense) {
		this.isSingleExpense = isSingleExpense;
	}

	public String getExpenseStatus() {
		return expenseStatus;
	}

	public void setExpenseStatus(String expenseStatus) {
		this.expenseStatus = expenseStatus;
	}

	public CreditCardTransactions getCreditCardTransactions() {
		return creditCardTransactions;
	}

	public void setCreditCardTransactions(CreditCardTransactions creditCardTransactions) {
		this.creditCardTransactions = creditCardTransactions;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}

	public BigDecimal getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(BigDecimal approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

}
