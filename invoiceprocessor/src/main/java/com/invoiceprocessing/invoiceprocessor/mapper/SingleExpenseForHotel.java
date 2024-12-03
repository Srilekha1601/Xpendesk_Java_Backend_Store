package com.invoiceprocessing.invoiceprocessor.mapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.invoiceprocessing.invoiceprocessor.model.FoodVoucherDetail;
import com.invoiceprocessing.invoiceprocessor.model.HotelVoucher;
import com.invoiceprocessing.invoiceprocessor.model.HotelVoucherDetail;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.model.VoucherEntity;
import com.invoiceprocessing.invoiceprocessor.repository.FoodVoucherDetailRepository;
import com.invoiceprocessing.invoiceprocessor.repository.HotelVoucherDetailRepository;
import com.invoiceprocessing.invoiceprocessor.repository.HotelVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherRepository;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDetailDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;

@Component("hotelForSingleExpense")
public class SingleExpenseForHotel implements VoucherWithOutTripMapper<VoucherWithOutTripDto, VoucherEntity> {

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	HotelVoucherRepository hotelVoucherRepository;

	@Autowired
	HotelVoucherDetailRepository hotelVoucherDetailRepository;

	@Autowired
	FoodVoucherDetailRepository foodVoucherDetailRepository;

	@Override
	public VoucherEntity dtoToModelWithOutTrip(VoucherWithOutTripDto singleVoucherForHotelDto) {
		// TODO Auto-generated method stub

		Voucher voucher = new Voucher();
		HotelVoucher hotelVoucher = new HotelVoucher();
		List<HotelVoucherDetail> listOfHotelVoucherDetails = new ArrayList<HotelVoucherDetail>();
		List<FoodVoucherDetail> listOfFoodVoucherDetails = new ArrayList<FoodVoucherDetail>();
		populateEntitiesFromDto(singleVoucherForHotelDto, voucher, hotelVoucher, listOfFoodVoucherDetails,
				listOfHotelVoucherDetails);
		voucher = voucherRepository.save(voucher);
		hotelVoucher = hotelVoucherRepository.save(hotelVoucher);
		hotelVoucherDetailRepository.saveAll(listOfHotelVoucherDetails);
		foodVoucherDetailRepository.saveAll(listOfFoodVoucherDetails);
		return hotelVoucher;

	}

	@Override
	public VoucherWithOutTripDto modelToDtoWithOutTrip(VoucherEntity target) {
		// TODO Auto-generated method stub
		return null;
	}

	private void populateEntitiesFromDto(VoucherWithOutTripDto voucher, Voucher voucherEntity,
			HotelVoucher hotelVoucherEntity, List<FoodVoucherDetail> hotelVoucherDetailListForFood,
			List<HotelVoucherDetail> hotelVoucherDetailList) {
//		trip = getTripByTripId(Integer.parseInt(voucher.getTripId()));
//		hotelCity = cityRepository.findByCityCode(voucher.getHotelCity());
//		hotelState = stateRepository.findByStateName(voucher.getHotelState());
		HotelVoucherDetail hotelVoucherDetailEntity = null;
		FoodVoucherDetail foodVoucherDetailEntity = null;

		voucherEntity.setFileName(voucher.getFilename());
		Long utilDate = new java.util.Date().getTime();
		voucherEntity.setVoucherDate(
				voucher.getDate().replace("/", " ").equalsIgnoreCase("Not Found") ? new java.sql.Date(utilDate)
						: parseStringToDate(voucher.getDate()));
		voucherEntity.setInvoiceNumber(voucher.getInvoiceNo());
		voucherEntity.setInvoiceType(voucher.getBillType());
		voucherEntity.setMerchantName(voucher.getMerchantName());
		voucherEntity.setInvoiceDate(parseStringToDate(voucher.getDate()));
		voucherEntity.setTotalAmount(new BigDecimal(voucher.getTotalAmount().replace(",", "")));
		voucherEntity.setHashText(voucher.getHashedText());
		voucherEntity.setPaymentMode(voucher.getPaymentMode());
		voucherEntity.setFileImage(voucher.getImage());
		voucherEntity.setManualEntry(voucher.getManualEntry());
		voucherEntity.setTotalPax(Integer.parseInt(voucher.getPax() == null ? "0" : voucher.getPax()));
//		voucherEntity.setEmployeeID(trip.getEmployeeID());
		voucherEntity.setClaimedAmount(
				new BigDecimal(voucher.getClaimedAmount() == null ? voucher.getTotalAmount().replace(",", "")
						: voucher.getClaimedAmount().replace(",", "")));

//		hotelVoucherEntity.setCityID(hotelCity);
//		hotelVoucherEntity.setStsteID(hotelState);
		hotelVoucherEntity.setCheckInDate(parseStringToDate(voucher.getCheckinDate()));
		hotelVoucherEntity.setCheckOutDate(parseStringToDate(voucher.getCheckoutDate()));
		hotelVoucherEntity.setGuestCompanyName(voucher.getCompanyName());
		hotelVoucherEntity.setGuestName(voucher.getGuestName());
		hotelVoucherEntity.setPinCode(voucher.getHotelPin());
//		hotelVoucherEntity.setVoucherID(voucherEntity);
//		hotelVoucherEntity = hotelVoucherrepo.save(hotelVoucherEntity);

//		Voucher voucherModel = voucherrepo.findById(Integer.parseInt(voucher.getVoucherID())).get();
//		Delete the records from the database...

//		insert the records in Db
		for (VoucherDetailDto hotelVoucherDetail : voucher.getConsolidatedHotelServiceBreakage()) {

			if (StringUtils.equalsAnyIgnoreCase(hotelVoucherDetail.getServiceType(), "Food")) {
				foodVoucherDetailEntity = new FoodVoucherDetail();
				foodVoucherDetailEntity.setHotelVoucher(hotelVoucherEntity);
				System.out.println(parseStringToDate(hotelVoucherDetail.getDate()));
				foodVoucherDetailEntity
						.setItemDate(hotelVoucherDetail.getDate().replace("/", " ").equalsIgnoreCase("not found")
								? new java.sql.Date(utilDate)
								: parseStringToDate(hotelVoucherDetail.getDate()));
				foodVoucherDetailEntity.setAmount(new BigDecimal(hotelVoucherDetail.getAmount().replace(",", "")));
				foodVoucherDetailEntity.setBillType(hotelVoucherDetail.getServiceType());
				foodVoucherDetailEntity.setDescription(hotelVoucherDetail.getDescription());
				foodVoucherDetailEntity.setCgstAmount(
						new BigDecimal(hotelVoucherDetail.getGst1() == null ? "0.0" : hotelVoucherDetail.getGst1()));
				foodVoucherDetailEntity.setSgstAmount(
						new BigDecimal(hotelVoucherDetail.getGst2() == null ? "0.0" : hotelVoucherDetail.getGst2()));
				foodVoucherDetailEntity.setGstAmount(new BigDecimal(hotelVoucherDetail.getGst1().replace(",", ""))
						.add(new BigDecimal(hotelVoucherDetail.getGst2().replace(",", ""))));
				hotelVoucherDetailListForFood.add(foodVoucherDetailEntity);
			} else {
				hotelVoucherDetailEntity = new HotelVoucherDetail();
				hotelVoucherDetailEntity.setAmount(new BigDecimal(hotelVoucherDetail.getAmount().replace(",", "")));
				hotelVoucherDetailEntity.setBillType(hotelVoucherDetail.getServiceType());
				hotelVoucherDetailEntity.setDate(parseStringToDate(hotelVoucherDetail.getDate()));
				hotelVoucherDetailEntity.setDescription(hotelVoucherDetail.getDescription());
				hotelVoucherDetailEntity.setSacCode(hotelVoucherDetail.getSacCode());
				hotelVoucherDetailEntity.setExceptionReason(hotelVoucherDetail.getExceptionReason());
				hotelVoucherDetailEntity.setCgstAmount(
						new BigDecimal(hotelVoucherDetail.getGst1() == null ? "0.0" : hotelVoucherDetail.getGst1()));
				hotelVoucherDetailEntity.setSgstAmount(
						new BigDecimal(hotelVoucherDetail.getGst2() == null ? "0.0" : hotelVoucherDetail.getGst2()));
				hotelVoucherDetailEntity.setGstAmount(new BigDecimal(hotelVoucherDetail.getGst1().replace(",", ""))
						.add(new BigDecimal(hotelVoucherDetail.getGst2().replace(",", ""))));
				hotelVoucherDetailEntity.setHotelVoucher(hotelVoucherEntity);
				hotelVoucherDetailList.add(hotelVoucherDetailEntity);
			}
		}

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

}
