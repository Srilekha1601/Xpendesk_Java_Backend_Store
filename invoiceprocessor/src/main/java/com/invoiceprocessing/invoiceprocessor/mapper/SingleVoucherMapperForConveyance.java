package com.invoiceprocessing.invoiceprocessor.mapper;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.invoiceprocessing.invoiceprocessor.model.ConveyanceVoucher;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.repository.ConveyanceVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherRepository;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;
import com.invoiceprocessing.invoiceprocessor.utils.XpendeskUtils;

@Mapper(componentModel = "spring")
public abstract class SingleVoucherMapperForConveyance {

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	ConveyanceVoucherRepository conveyanceVoucherRepository;

	@Mapping(target = "distance", source = "voucherWithOutTripDto.noOfKm")
	@Mapping(target = "voucherID", source = "voucherWithOutTripDto", qualifiedByName = "stringToVoucher")
//	@Mapping(target = "departureTime", source = "voucherWithOutTripDto.departureTime", qualifiedByName = "stringToTime")
	@Mapping(target = "departureDate", source = "voucherWithOutTripDto.departureDate", qualifiedByName = "stringToDate")
//	@Mapping(target = "arrivalTime", source = "voucherWithOutTripDto.arrivalTime", qualifiedByName = "stringToTime")
	@Mapping(target = "arrivalDate", source = "voucherWithOutTripDto.arrivalDate", qualifiedByName = "stringToDate")
	@Mapping(target = "conveyanceVoucherID", source = "voucherWithOutTripDto", qualifiedByName = "conveyanceVoucherIsEmptyCheck")
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdOn", ignore = true)
	@Mapping(target = "isDeleted", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "updatedOn", ignore = true)
	public abstract ConveyanceVoucher convertConveyanceVoucherDtoToConveyanceVoucher(
			VoucherWithOutTripDto voucherWithOutTripDto);

	@InheritInverseConfiguration
	@Mapping(target = "voucherID", source = "conveyanceVoucher.voucherID", qualifiedByName = "voucherToString")
	@Mapping(target = "arrivalDate", source = "conveyanceVoucher.arrivalDate", qualifiedByName = "dateToString")
//	@Mapping(target = "arrivalTime", source = "conveyanceVoucher.arrivalTime", qualifiedByName = "timeToStringConverter")
	@Mapping(target = "departureDate", source = "conveyanceVoucher.departureDate", qualifiedByName = "dateToString")
//	@Mapping(target = "departureTime", source = "conveyanceVoucher.departureTime", qualifiedByName = "timeToStringConverter")
	@Mapping(target = "billType", source = "conveyanceVoucher.voucherID", qualifiedByName = "voucherBillTypeToString")
	@Mapping(target = "claimedAmount", source = "conveyanceVoucher.voucherID", qualifiedByName = "voucherClaimedAmountToString")
	@Mapping(target = "totalAmount", source = "conveyanceVoucher.voucherID", qualifiedByName = "voucherTotalAmountToString")
	@Mapping(target = "companyName", source = "conveyanceVoucher.voucherID", qualifiedByName = "voucherCompanyNameToString")
	@Mapping(target = "exceptionReason", source = "conveyanceVoucher.voucherID", qualifiedByName = "voucherExceptionReasonToString")
	@Mapping(target = "filename", source = "conveyanceVoucher.voucherID", qualifiedByName = "voucherFileNameToString")
	@Mapping(target = "hashedText", source = "conveyanceVoucher.voucherID", qualifiedByName = "voucherHashTextToString")
	@Mapping(target = "date", source = "conveyanceVoucher.voucherID", qualifiedByName = "voucherInvoiceDateToString")
	@Mapping(target = "invoiceNo", source = "conveyanceVoucher.voucherID", qualifiedByName = "voucherInvoiceNumberToString")
	@Mapping(target = "travelTicketClass", source = "conveyanceVoucher.travelClass")
	@Mapping(target = "cgstAmount", ignore = true)
	@Mapping(target = "checkinDate", ignore = true)
	@Mapping(target = "checkinDateFormat", ignore = true)
	@Mapping(target = "checkoutDate", ignore = true)
	@Mapping(target = "checkoutDateFormat", ignore = true)
	@Mapping(target = "consolidatedHotelServiceBreakage", ignore = true)
	@Mapping(target = "currency", ignore = true)
	@Mapping(target = "dateFormat", ignore = true)
	@Mapping(target = "description", ignore = true)
	@Mapping(target = "foodVoucherId", ignore = true)
	@Mapping(target = "gstFormat", ignore = true)
	@Mapping(target = "gstNo", ignore = true)
	@Mapping(target = "guestName", ignore = true)
	@Mapping(target = "hotelCity", ignore = true)
	@Mapping(target = "hotelPin", ignore = true)
	@Mapping(target = "hotelState", ignore = true)
	@Mapping(target = "image", ignore = true)
	@Mapping(target = "intraOrInterCityTravel", ignore = true)
	@Mapping(target = "isSingleExpense", ignore = true)
	@Mapping(target = "liquorStatus", ignore = true)
	@Mapping(target = "manualEntry", ignore = true)
	@Mapping(target = "merchantName", ignore = true)
	@Mapping(target = "pax", ignore = true)
	@Mapping(target = "sgstAmount", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "totalAmountFormat", ignore = true)
	@Mapping(target = "totalDaysStayed", ignore = true)
	@Mapping(target = "employeeId", ignore = true)
	@Mapping(target = "hotelServiceBreakage", ignore = true)
	@Mapping(target = "isOthersClassified", ignore = true)
	@Mapping(target = "tripID", ignore = true)
	public abstract VoucherWithOutTripDto convertConveyanceVoucherToConveyanceVoucherDto(
			ConveyanceVoucher conveyanceVoucher);

	@Named("stringToVoucher")
	protected Voucher stringToVoucher(VoucherWithOutTripDto voucherWithOutTripDto) {
		return voucherRepository.findById(Integer.parseInt(voucherWithOutTripDto.getVoucherID())).get();
	}

	@Named("stringToTime")
	protected Time stringToTime(String time) {
		return XpendeskUtils.parseTime(time);
	}

	@Named("stringToDate")
	public Date stringToDate(String dateStr) {
		return XpendeskUtils.parseStringToDate(dateStr);
	}

	@Named("voucherToString")
	protected String voucherToString(Voucher voucher) {
		return voucher.getVoucherID().toString();
	}

	@Named("voucherBillTypeToString")
	protected String voucherBillTypeToString(Voucher voucher) {
		return voucherRepository.findById(voucher.getVoucherID()).get().getInvoiceType();
	}

	@Named("voucherClaimedAmountToString")
	protected String voucherClaimedAmountToString(Voucher voucher) {
		return voucherRepository.findById(voucher.getVoucherID()).get().getClaimedAmount().toString();
	}

	@Named("voucherTotalAmountToString")
	protected String voucherTotalAmountToString(Voucher voucher) {
		return voucherRepository.findById(voucher.getVoucherID()).get().getTotalAmount().toString();
	}

	@Named("voucherCompanyNameToString")
	protected String voucherCompanyNameToString(Voucher voucher) {
		return voucherRepository.findById(voucher.getVoucherID()).get().getMerchantName();
	}

	@Named("voucherExceptionReasonToString")
	protected String voucherExceptionReasonToString(Voucher voucher) {
		return voucherRepository.findById(voucher.getVoucherID()).get().getExceptionReason();
	}

	@Named("voucherFileNameToString")
	protected String voucherFileNameToString(Voucher voucher) {
		return voucherRepository.findById(voucher.getVoucherID()).get().getFileName();
	}

	@Named("voucherHashTextToString")
	protected String voucherHashTextToString(Voucher voucher) {
		return voucherRepository.findById(voucher.getVoucherID()).get().getHashText();
	}

	@Named("voucherInvoiceDateToString")
	protected String voucherInvoiceDateToString(Voucher voucher) {
		return dateToString(voucherRepository.findById(voucher.getVoucherID()).get().getInvoiceDate());
	}

	@Named("voucherInvoiceNumberToString")
	protected String voucherInvoiceNumberToString(Voucher voucher) {
		return voucherRepository.findById(voucher.getVoucherID()).get().getInvoiceNumber();
	}

	@Named("dateToString")
	protected String dateToString(Date date) {
		return DateMapper.dateToString(date);
	}

	@Named("timeToStringConverter")
	protected String timeToStringConverter(Time time) {
		return new SimpleDateFormat("HH:mm:ss").format(time);
	}

	@Named("conveyanceVoucherIsEmptyCheck")
	protected Integer conveyanceVoucherIsEmptyCheck(VoucherWithOutTripDto voucherWithOutTripDto) {
		return voucherWithOutTripDto.getConveyanceVoucherID().isEmpty() ? null
				: conveyanceVoucherRepository.findById(Integer.parseInt(voucherWithOutTripDto.getVoucherID())).get()
						.getConveyanceVoucherID();
	}
}