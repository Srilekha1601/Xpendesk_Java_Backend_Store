package com.invoiceprocessing.invoiceprocessor.mapper;

import java.math.BigDecimal;
import java.util.Date;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.invoiceprocessing.invoiceprocessor.model.ConveyanceVoucher;
import com.invoiceprocessing.invoiceprocessor.model.CreditCardTransactions;
import com.invoiceprocessing.invoiceprocessor.model.Employee;
import com.invoiceprocessing.invoiceprocessor.model.FoodVoucher;
import com.invoiceprocessing.invoiceprocessor.model.HotelVoucher;
import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.repository.ConveyanceVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.CreditCardTransactionsRepository;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.FoodVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.HotelVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherRepository;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;
import com.invoiceprocessing.invoiceprocessor.utils.XpendeskUtils;

@Mapper(componentModel = "spring")
public abstract class AllVoucherMapperForSingleExpense {

	@Autowired
	CreditCardTransactionsRepository creditCardTransactionsRepository;

	@Autowired
	TripRepository tripRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	HotelVoucherRepository hotelVoucherRepository;

	@Autowired
	FoodVoucherRepository foodVoucherRepository;

	@Autowired
	ConveyanceVoucherRepository conveyanceVoucherRepository;

	@Autowired
	VoucherRepository voucherRepository;

	@Mapping(target = "fileImage", source = "voucherWithOutTripDto.image")
	@Mapping(target = "hashText", source = "voucherWithOutTripDto.hashedText")
	@Mapping(target = "fileName", source = "voucherWithOutTripDto.filename")
	@Mapping(target = "invoiceDate", source = "voucherWithOutTripDto.date", qualifiedByName = "stringToDate")
	@Mapping(target = "invoiceNumber", source = "voucherWithOutTripDto.invoiceNo")
	@Mapping(target = "invoiceType", source = "voucherWithOutTripDto.billType")
	@Mapping(target = "totalPax", source = "voucherWithOutTripDto.pax")
	@Mapping(target = "voucherDate", source = "voucherWithOutTripDto.date", qualifiedByName = "stringToDate")
	@Mapping(target = "claimedAmount", source = "voucherWithOutTripDto", qualifiedByName = "claimedAmountCheck")
	@Mapping(target = "voucherID", source = "voucherWithOutTripDto.voucherID", qualifiedByName = "getVoucherById")
	@Mapping(target = "creditCardTransactions", source = "voucherWithOutTripDto", qualifiedByName = "saveCreditCardIfCCTransactionIsPresent")
	@Mapping(target = "deleteStatus", source = "voucherWithOutTripDto.deleteStatus", qualifiedByName = "saveDeleteStatusForCCTransaction")
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdOn", ignore = true)
	@Mapping(target = "hotelVoucherID", ignore = true)
	@Mapping(target = "foodVoucherID", ignore = true)
	@Mapping(target = "conveyanceVoucherID", ignore = true)
	@Mapping(target = "isDeleted", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "expenseStatus", ignore = true)
	@Mapping(target = "reason", ignore = true)
	@Mapping(target = "updatedOn", ignore = true)
	@Mapping(target = "trip", ignore = true)
	@Mapping(target = "employeeID", source = "voucherWithOutTripDto", qualifiedByName = "getEmployeeFromEmployeeId")
	public abstract Voucher convertSingleVoucherDtoToVoucher(VoucherWithOutTripDto voucherWithOutTripDto);

	@InheritInverseConfiguration
	@Mapping(target = "date", source = "voucher.invoiceDate", qualifiedByName = "dateToString")
	@Mapping(target = "voucherID", source = "voucher.voucherID", qualifiedByName = "integerToString")
	@Mapping(target = "conveyanceVoucherID", ignore = true)
	@Mapping(target = "liquorStatus", ignore = true)
	@Mapping(target = "arrivalDate", ignore = true)
	@Mapping(target = "arrivalTime", ignore = true)
	@Mapping(target = "checkinDate", ignore = true)
	@Mapping(target = "checkinDateFormat", ignore = true)
	@Mapping(target = "checkoutDate", ignore = true)
	@Mapping(target = "checkoutDateFormat", ignore = true)
	@Mapping(target = "companyName", ignore = true)
	@Mapping(target = "currency", ignore = true)
	@Mapping(target = "dateFormat", ignore = true)
	@Mapping(target = "departureDate", ignore = true)
	@Mapping(target = "consolidatedHotelServiceBreakage", ignore = true)
	@Mapping(target = "departureTime", ignore = true)
	@Mapping(target = "description", ignore = true)
	@Mapping(target = "fromLocation", ignore = true)
	@Mapping(target = "gstFormat", ignore = true)
	@Mapping(target = "gstNo", ignore = true)
	@Mapping(target = "guestName", ignore = true)
	@Mapping(target = "hashedText", ignore = true)
	@Mapping(target = "hotelCity", ignore = true)
	@Mapping(target = "hotelPin", ignore = true)
	@Mapping(target = "hotelState", ignore = true)
	@Mapping(target = "sgstAmount", ignore = true)
	@Mapping(target = "cgstAmount", ignore = true)
	@Mapping(target = "interIntraCity", ignore = true)
	@Mapping(target = "intraOrInterCityTravel", ignore = true)
	@Mapping(target = "invoiceNo", source = "voucher.invoiceNumber")
	@Mapping(target = "isSingleExpense", source = "voucher.isSingleExpense")
	@Mapping(target = "manualEntry", source = "voucher.manualEntry")
	@Mapping(target = "modeOfTravel", ignore = true)
	@Mapping(target = "noOfKm", ignore = true)
	@Mapping(target = "pax", source = "voucher.totalPax")
	@Mapping(target = "toLocation", ignore = true)
	@Mapping(target = "totalAmountFormat", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "totalDaysStayed", ignore = true)
	@Mapping(target = "employeeId", ignore = true)
	@Mapping(target = "hotelServiceBreakage", ignore = true)
	@Mapping(target = "accomodationCategory", ignore = true)
	@Mapping(target = "isOthersClassified", ignore = true)
	@Mapping(target = "ccTransactionId", ignore = true)
	@Mapping(target = "travelTicketClass", ignore = true)
	@Mapping(target = "tripID", ignore = true)
	@Mapping(target = "foodVoucherId", ignore = true)
	@Mapping(target = "hotelVoucherId", ignore = true)
	@Mapping(target = "travelClass", ignore = true)
	public abstract VoucherWithOutTripDto convertVoucherToVoucherWithOutTripDto(Voucher voucher);

	@Named("getTripFromTripId")
	protected Trip getTripFromTripId(String tripId) {
		return tripRepository.findById(Integer.parseInt(tripId)).get();
	}

	@Named("getEmployeeFromEmployeeId")
	protected Employee getEmployeeFromEmployeeId(VoucherWithOutTripDto voucherWithOutTripDto) {
		return voucherWithOutTripDto.getEmployeeId() != null
				? employeeRepository.findById(voucherWithOutTripDto.getEmployeeId()).get()
				: null;
	}

	@Named("stringToDate")
	protected Date stringToDate(String dateStr) {
		return XpendeskUtils.parseStringToDate(dateStr);
	}

	@Named("stringToBigDecimal")
	protected BigDecimal stringToBigDecimal(String amount) {
		return new BigDecimal(amount);
	}

	@Named("getHotelVoucherFromId")
	protected HotelVoucher getHotelVoucherFromId(String voucherId) {
		return voucherId.isEmpty() ? null : hotelVoucherRepository.findById(Integer.parseInt(voucherId)).get();
	}

	@Named("getConveyanceVoucherById")
	protected ConveyanceVoucher getConveyanceVoucherById(String voucherId) {
		return voucherId.isEmpty() ? null : conveyanceVoucherRepository.findById(Integer.parseInt(voucherId)).get();
	}

	@Named("getVoucherById")
	protected Integer getVoucherById(String voucherId) {
		return voucherId.isEmpty() ? null
				: voucherRepository.findById(Integer.parseInt(voucherId)).get().getVoucherID();
	}

	@Named("getTripIdFromTrip")
	protected String getTripIdFromTrip(Trip trip) {
		return trip.getTripID().toString();
	}

	@Named("getConveyanceVoucherFromId")
	protected String getConveyanceVoucherFromId(ConveyanceVoucher conveyanceVoucher) {
		return conveyanceVoucher.getConveyanceVoucherID().toString();
	}

	@Named("getFoodVoucherById")
	protected String getFoodVoucherById(FoodVoucher foodVoucher) {
		return foodVoucher.getFoodVoucherID().toString();
	}

	@Named("getHotelVoucherById")
	protected String getHotelVoucherById(HotelVoucher hotelVoucher) {
		return hotelVoucher.getHotelVoucherID().toString();
	}

	@Named("dateToString")
	protected String dateToString(Date date) {
		return DateMapper.dateToString(date);
	}

	@Named("stringToInteger")
	protected Integer stringToInteger(String voucherDetailId) {
		return voucherDetailId.isEmpty() ? null : Integer.parseInt(voucherDetailId);
	}

	@Named("integerToString")
	protected String integerToString(Integer voucherId) {
		return voucherId.toString();
	}

	@Named("claimedAmountCheck")
	protected BigDecimal claimedAmountCheck(VoucherWithOutTripDto voucherWithOutTripDto) {
		return new BigDecimal(
				voucherWithOutTripDto.getClaimedAmount() == null
						? voucherWithOutTripDto.getTotalAmount().equalsIgnoreCase("Not Found") ? "0.0"
								: voucherWithOutTripDto.getTotalAmount()
						: voucherWithOutTripDto.getClaimedAmount());
	}

	@Named("saveCreditCardIfCCTransactionIsPresent")
	protected CreditCardTransactions saveCreditCardIfCCTransactionIsPresent(
			VoucherWithOutTripDto voucherWithOutTripDto) {
		return voucherWithOutTripDto.getCcTransactionId() != null ? creditCardTransactionsRepository
				.findById(Integer.parseInt(voucherWithOutTripDto.getCcTransactionId())).get() : null;
	}

	@Named("saveDeleteStatusForCCTransaction")
	protected String saveDeleteStatusForCCTransaction(String deleteStatus) {
		return deleteStatus == null || deleteStatus.equalsIgnoreCase("") || deleteStatus.equalsIgnoreCase("N") ? "N"
				: deleteStatus;
	}

}
