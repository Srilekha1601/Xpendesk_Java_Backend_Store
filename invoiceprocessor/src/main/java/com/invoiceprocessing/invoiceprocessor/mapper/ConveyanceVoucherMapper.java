package com.invoiceprocessing.invoiceprocessor.mapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
//import java.util.Optional;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.invoiceprocessing.invoiceprocessor.model.ConveyanceVoucher;
import com.invoiceprocessing.invoiceprocessor.model.Trip;
//import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.repository.ConveyanceVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherRepository;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;

@Component("conveyanceVoucherMapper")
public class ConveyanceVoucherMapper implements VoucherMapper<VoucherDto, Voucher> {

	@Autowired
	VoucherRepository voucherRepository;
	@Autowired
	ConveyanceVoucherRepository conveyanceVoucherRepository;
	@Autowired
	TripRepository tripRepository;

	@Override
	public Voucher dtoToModel(VoucherDto conveyanceVoucherDto) {

		Voucher voucher = new Voucher();
		ConveyanceVoucher conveyanceVoucher = new ConveyanceVoucher();

		if (conveyanceVoucherDto != null) {
//			if (conveyanceVoucherDto.getVoucherID() == null)
//				conveyanceVoucherDto.setVoucherID("");
//			if (conveyanceVoucherDto.getVoucherID().isEmpty()) {
			dataInsertForConveyance(conveyanceVoucherDto, voucher, conveyanceVoucher);
			voucher = voucherRepository.save(voucher);
			conveyanceVoucher = conveyanceVoucherRepository.save(conveyanceVoucher);
			voucher.setConveyanceVoucherID(conveyanceVoucher);
//			} else {
//				voucher = voucherRepository.findById(Integer.parseInt(conveyanceVoucherDto.getVoucherID())).get();
//				conveyanceVoucher = voucher.getConveyanceVoucherID();
//				dataInsertForConveyance(conveyanceVoucherDto, voucher, conveyanceVoucher);
//				voucher = voucherRepository.save(voucher);
//				conveyanceVoucher = conveyanceVoucherRepository.save(conveyanceVoucher);
//			}

		} else {
			return null;
		}

		return voucher;
	}

	@Override
	public VoucherDto modelToDto(Voucher target) {
		return null;
	}

	private static Date parseStringToDate(String date) {
		Date outDate = null;
		try {
			outDate = new java.sql.Date(((java.util.Date) new SimpleDateFormat("dd-MM-yyyy").parse(date)).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return outDate;
	}

//	private String converterAnyFormat(String date) {
//		String outPutDate = null;
//		SimpleDateFormat outputDateFromat = new SimpleDateFormat("dd-MM-yyyy");
//		if (date.matches("\\d{2}-\\D{3}-\\d{4}")) {
//			SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
//			try {
//				java.util.Date inputDate = inputDateFormat.parse(date);
//				return outputDateFromat.format(inputDate);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		} else if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
//			SimpleDateFormat inputDateFormatForYYYY = new SimpleDateFormat("yyyy-MM-dd");
//			try {
//				java.util.Date ipDate = inputDateFormatForYYYY.parse(date);
//				return outputDateFromat.format(ipDate);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		} else if (date.matches("\\d{2}/\\d{2}/\\d{4}")) {
//			SimpleDateFormat inputDateFormatFordd = new SimpleDateFormat("dd/MM/yyyy");
//			try {
//				java.util.Date ipDateFordd = inputDateFormatFordd.parse(date);
//				String opDateFormatFordd = outputDateFromat.format(ipDateFordd);
//				return opDateFormatFordd;
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		} else if (date.matches("\\d{4}/\\d{2}/\\d{2}")) {
//			SimpleDateFormat inputDateFormatForYY = new SimpleDateFormat("yyyy/MM/dd");
//			try {
//				java.util.Date ipDateForYY = inputDateFormatForYY.parse(date);
//				String opDateFormatForYY = outputDateFromat.format(ipDateForYY);
//				return opDateFormatForYY;
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		} else {
//			SimpleDateFormat inputDateFormatForddS = new SimpleDateFormat("dd-MM-yyyy");
//			try {
//				java.util.Date ipDateForddS = inputDateFormatForddS.parse(date);
//				outPutDate = outputDateFromat.format(ipDateForddS);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}
//		return outPutDate;
//	}

	private static Date getCurrentDate() {
		java.sql.Date dateSql = null;
		LocalDate dateObj = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date = dateObj.format(formatter);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		try {
			java.util.Date utilDate = format.parse(date);
			dateSql = new java.sql.Date(utilDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateSql;
	}

	public static Time parseTime(String inputTime) {
		final String TIME_FORMAT_24HR = "HH:mm";
		final String TIME_FORMAT_24HRSS = "HH:mm:ss";
		final String TIME_FORMAT_12HR = "hh:mm a";
		final String TIME_FORMAT_12HRSS = "hh:mm:ss a";
		SimpleDateFormat sdf = null;

		try {
			sdf = new SimpleDateFormat(TIME_FORMAT_24HR);
			return new Time(sdf.parse(inputTime).getTime());
		} catch (ParseException e) {
			try {
				sdf = new SimpleDateFormat(TIME_FORMAT_24HRSS);
				return new Time(sdf.parse(inputTime).getTime());
			} catch (ParseException e1) {

				try {
					sdf = new SimpleDateFormat(TIME_FORMAT_12HR);
					return new Time(sdf.parse(inputTime).getTime());
				} catch (ParseException e2) {
					try {
						sdf = new SimpleDateFormat(TIME_FORMAT_12HRSS);
						return new Time(sdf.parse(inputTime).getTime());
					} catch (ParseException e3) {
						// Invalid format - to add logger error
					}
				}
			}
		}

		return null;
	}

	private Trip getTripByTripId(Integer tripId) {
		Optional<Trip> tripEntity = tripRepository.findById(tripId);
		return tripEntity.get();
	}

	private void dataInsertForConveyance(VoucherDto conveyanceVoucherDto, Voucher voucher,
			ConveyanceVoucher conveyanceVoucher) {
		if (!conveyanceVoucherDto.getVoucherID().isEmpty())
			voucher.setVoucherID(Integer.parseInt(conveyanceVoucherDto.getVoucherID()));
//		voucher.setClaimedAmount(new BigDecimal(conveyanceVoucherDto.getClaimedAmount() == null ? "0.0"
//				: conveyanceVoucherDto.getClaimedAmount().replace(",", "")));

		voucher.setClaimedAmount(new BigDecimal(conveyanceVoucherDto.getTotalAmount()));
		voucher.setExceptionReason(conveyanceVoucherDto.getExceptionReason() == null ? "No Exception"
				: conveyanceVoucherDto.getExceptionReason());
		voucher.setFileName(conveyanceVoucherDto.getFilename());
		voucher.setPaymentMode(conveyanceVoucherDto.getPaymentMode());
		voucher.setHashText(conveyanceVoucherDto.getHashedText());
		voucher.setInvoiceType(conveyanceVoucherDto.getBillType());
		voucher.setInvoiceNumber(conveyanceVoucherDto.getInvoiceNo());
		voucher.setTotalAmount(new BigDecimal(conveyanceVoucherDto.getTotalAmount()));
		voucher.setMerchantName(conveyanceVoucherDto.getMerchantName());
		voucher.setInvoiceDate(parseStringToDate(conveyanceVoucherDto.getDate()));
		voucher.setVoucherDate(parseStringToDate(conveyanceVoucherDto.getDate()));
		voucher.setFileImage(conveyanceVoucherDto.getImage());
		voucher.setManualEntry(conveyanceVoucherDto.getManualEntry());
//		voucher.setDeleteStatus("N");
		voucher.setDeleteStatus(conveyanceVoucherDto.getDeleteStatus() == null
				|| conveyanceVoucherDto.getDeleteStatus().equalsIgnoreCase("")
				|| conveyanceVoucherDto.getDeleteStatus().equalsIgnoreCase("N") ? "N"
						: conveyanceVoucherDto.getDeleteStatus());

//		....................changes made here..................
		if (conveyanceVoucherDto.getIsSingleExpense().equalsIgnoreCase("Y"))
			voucher.setIsSingleExpense(conveyanceVoucherDto.getIsSingleExpense());
		voucher.setIsSingleExpense(conveyanceVoucherDto.getIsSingleExpense());

//		....................changes made here...................
		if (!conveyanceVoucherDto.getTripId().isEmpty())
			voucher.setEmployeeID(
					tripRepository.findById(Integer.parseInt(conveyanceVoucherDto.getTripId())).get().getEmployeeID());
		if (!conveyanceVoucherDto.getTripId().isEmpty())
			voucher.setTrip(getTripByTripId(Integer.parseInt(conveyanceVoucherDto.getTripId())));

		if (!conveyanceVoucherDto.getConveyanceVoucherID().isEmpty())
			conveyanceVoucher.setConveyanceVoucherID(Integer.parseInt(conveyanceVoucherDto.getConveyanceVoucherID()));
		conveyanceVoucher
				.setDistance(new BigDecimal(conveyanceVoucherDto.getNoOfKm().equalsIgnoreCase("Not Found") ? "0.0"
						: conveyanceVoucherDto.getNoOfKm()));
		conveyanceVoucher.setFromLocation(conveyanceVoucherDto.getFromLocation());
		conveyanceVoucher.setModeOfTravel(conveyanceVoucherDto.getModeOfTravel());
		conveyanceVoucher.setVehicleFuelType(conveyanceVoucherDto.getVehicleFuelType());
		conveyanceVoucher.setVehicleType(conveyanceVoucherDto.getVehicleType());
		conveyanceVoucher.setToLocation(conveyanceVoucherDto.getToLocation());
		conveyanceVoucher.setTravelClass(conveyanceVoucherDto.getTravelTicketClass());
		conveyanceVoucher.setVoucherID(voucher);
		conveyanceVoucher
				.setArrivalDate(conveyanceVoucherDto.getArrivalDate().equalsIgnoreCase("Not Found") ? getCurrentDate()
						: parseStringToDate(conveyanceVoucherDto.getArrivalDate()));
//		conveyanceVoucher.setArrivalTime(parseTime(conveyanceVoucherDto.getArrivalTime()));
//		conveyanceVoucher.setDepartureTime(parseTime(conveyanceVoucherDto.getArrivalTime()));
		conveyanceVoucher.setArrivalTime(
				conveyanceVoucherDto.getArrivalTime() != null ? conveyanceVoucherDto.getArrivalTime() : "");
		conveyanceVoucher.setDepartureTime(
				conveyanceVoucherDto.getDepartureTime() != null ? conveyanceVoucherDto.getDepartureTime() : "");
		conveyanceVoucher.setInterIntraCity(conveyanceVoucherDto.getIntraOrInterCityTravel());
		conveyanceVoucher.setDepartureDate(
				conveyanceVoucherDto.getDepartureDate().equalsIgnoreCase("Not Found") ? getCurrentDate()
						: parseStringToDate(conveyanceVoucherDto.getDepartureDate()));
	}
}
