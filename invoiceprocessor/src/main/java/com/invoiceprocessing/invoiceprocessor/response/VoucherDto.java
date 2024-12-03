package com.invoiceprocessing.invoiceprocessor.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VoucherDto {

	private String filename;
	private String billType;
	private String merchantName;
	private String guestName;
	private String invoiceNo;
	private String gstNo;
	private String companyName;
	private String totalAmount;
	private String cabType;
	private String accommodationType;
	private String date;
	private String checkinDate;
	private String checkoutDate;
//	@JsonDeserialize(using = TotalDaysStayedDeserializer.class)
	private String totalDaysStayed;
	private String modeOfTravel;
	private String vehicleType;
	private String travelTicketClass;
	private String fromLocation;
	private String toLocation;
	private String departureDate;
	private String departureTime;
	private String arrivalDate;
	private String arrivalTime;
	private String noOfKm;
	private String vehicleFuelType;
	private String intraOrInterCityTravel;
	private String hotelState;
	private String hotelCity;
	private String hotelPin;
	private String currency;
	private String travelClass;
	private String interIntraCity;
	private String allow;
	private String tripId;
	private String voucherID;
	private String foodVoucherID;
	private String hotelVoucherID;
	private String conveyanceVoucherID;
	private String cgstAmount;
	private String sgstAmount;
	private String pax;
	private String expenseStatus;
	private String paymentMode;
	private String isApproved;
//	@JsonIgnore
//	private String hotelServiceBreakage;
	private List<VoucherDetailDto> consolidatedHotelServiceBreakage;
	private String totalAmountFormat;
	private int dateFormat;
	private int checkinDateFormat;
	private int checkoutDateFormat;
	private String gstFormat;
	private String liquorStatus;
	private String deleteStatus;
	private String hashedText;
	private String isDuplicate;
	private int status;
	private String manualEntry;
	private String isSingleExpense;
	private String claimedAmount;
	private String objectionRaise;
	private String description;
	private String ccTransactionId;
	private String exceptionReason;
	private byte[] image;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getGstNo() {
		return gstNo;
	}

	public String getFoodVoucherID() {
		return foodVoucherID;
	}

	public void setFoodVoucherID(String foodVoucherID) {
		this.foodVoucherID = foodVoucherID;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(String checkinDate) {
		this.checkinDate = checkinDate;
	}

	public String getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(String checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public String getTotalDaysStayed() {
		return totalDaysStayed;
	}

	public void setTotalDaysStayed(String totalDaysStayed) {
		this.totalDaysStayed = totalDaysStayed;
	}

	public String getModeOfTravel() {
		return modeOfTravel;
	}

	public void setModeOfTravel(String modeOfTravel) {
		this.modeOfTravel = modeOfTravel;
	}

	public String getTravelTicketClass() {
		return travelTicketClass;
	}

	public void setTravelTicketClass(String travelTicketClass) {
		this.travelTicketClass = travelTicketClass;
	}

	public String getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(String fromLocation) {
		this.fromLocation = fromLocation;
	}

	public String getToLocation() {
		return toLocation;
	}

	public void setToLocation(String toLocation) {
		this.toLocation = toLocation;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getNoOfKm() {
		return noOfKm;
	}

	public void setNoOfKm(String noOfKm) {
		this.noOfKm = noOfKm;
	}

	public String getIntraOrInterCityTravel() {
		return intraOrInterCityTravel;
	}

	public void setIntraOrInterCityTravel(String intraOrInterCityTravel) {
		this.intraOrInterCityTravel = intraOrInterCityTravel;
	}

	public String getHotelState() {
		return hotelState;
	}

	public void setHotelState(String hotelState) {
		this.hotelState = hotelState;
	}

	public String getHotelCity() {
		return hotelCity;
	}

	public void setHotelCity(String hotelCity) {
		this.hotelCity = hotelCity;
	}

	public String getHotelPin() {
		return hotelPin;
	}

	public void setHotelPin(String hotelPin) {
		this.hotelPin = hotelPin;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTotalAmountFormat() {
		return totalAmountFormat;
	}

	public void setTotalAmountFormat(String totalAmountFormat) {
		this.totalAmountFormat = totalAmountFormat;
	}

	public int getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(int dateFormat) {
		this.dateFormat = dateFormat;
	}

	public int getCheckinDateFormat() {
		return checkinDateFormat;
	}

	public void setCheckinDateFormat(int checkinDateFormat) {
		this.checkinDateFormat = checkinDateFormat;
	}

	public int getCheckoutDateFormat() {
		return checkoutDateFormat;
	}

	public void setCheckoutDateFormat(int checkoutDateFormat) {
		this.checkoutDateFormat = checkoutDateFormat;
	}

	public String getGstFormat() {
		return gstFormat;
	}

	public void setGstFormat(String gstFormat) {
		this.gstFormat = gstFormat;
	}

	public String getLiquorStatus() {
		return liquorStatus;
	}

	public void setLiquorStatus(String liquorStatus) {
		this.liquorStatus = liquorStatus;
	}

	public String getHashedText() {
		return hashedText;
	}

	public void setHashedText(String hashedText) {
		this.hashedText = hashedText;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTravelClass() {
		return travelClass;
	}

	public void setTravelClass(String travelClass) {
		this.travelClass = travelClass;
	}

	public String getInterIntraCity() {
		return interIntraCity;
	}

	public void setInterIntraCity(String interIntraCity) {
		this.interIntraCity = interIntraCity;
	}

	public List<VoucherDetailDto> getConsolidatedHotelServiceBreakage() {
		return consolidatedHotelServiceBreakage;
	}

	public void setConsolidatedHotelServiceBreakage(List<VoucherDetailDto> consolidatedHotelServiceBreakage) {
		this.consolidatedHotelServiceBreakage = consolidatedHotelServiceBreakage;
	}

	public String getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(String claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExceptionReason() {
		return exceptionReason;
	}

	public void setExceptionReason(String exceptionReason) {
		this.exceptionReason = exceptionReason;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getVoucherID() {
		return voucherID;
	}

	public void setVoucherID(String voucherID) {
		this.voucherID = voucherID;
	}

	public String getManualEntry() {
		return manualEntry;
	}

	public void setManualEntry(String manualEntry) {
		this.manualEntry = manualEntry;
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

	public String getPax() {
		return pax;
	}

	public void setPax(String pax) {
		this.pax = pax;
	}

	public String getIsSingleExpense() {
		return isSingleExpense;
	}

	public void setIsSingleExpense(String isSingleExpense) {
		this.isSingleExpense = isSingleExpense;
	}

	public String getHotelVoucherID() {
		return hotelVoucherID;
	}

	public void setHotelVoucherID(String hotelVoucherID) {
		this.hotelVoucherID = hotelVoucherID;
	}

	public String getConveyanceVoucherID() {
		return conveyanceVoucherID;
	}

	public void setConveyanceVoucherID(String conveyanceVoucherID) {
		this.conveyanceVoucherID = conveyanceVoucherID;
	}

	public String getCabType() {
		return cabType;
	}

	public void setCabType(String cabType) {
		this.cabType = cabType;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getAccommodationType() {
		return accommodationType;
	}

	public void setAccommodationType(String accommodationType) {
		this.accommodationType = accommodationType;
	}

	public String getExpenseStatus() {
		return expenseStatus;
	}

	public void setExpenseStatus(String expenseStatus) {
		this.expenseStatus = expenseStatus;
	}

	public String getIsDuplicate() {
		return isDuplicate;
	}

	public void setIsDuplicate(String isDuplicate) {
		this.isDuplicate = isDuplicate;
	}

	public String getAllow() {
		return allow;
	}

	public void setAllow(String allow) {
		this.allow = allow;
	}

	public String getVehicleFuelType() {
		return vehicleFuelType;
	}

	public void setVehicleFuelType(String vehicleFuelType) {
		this.vehicleFuelType = vehicleFuelType;
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

	public String getObjectionRaise() {
		return objectionRaise;
	}

	public void setObjectionRaise(String objectionRaise) {
		this.objectionRaise = objectionRaise;
	}

	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}

	public String getCcTransactionId() {
		return ccTransactionId;
	}

	public void setCcTransactionId(String ccTransactionId) {
		this.ccTransactionId = ccTransactionId;
	}

}