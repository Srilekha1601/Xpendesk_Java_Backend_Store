package com.invoiceprocessing.invoiceprocessor.mapper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.invoiceprocessing.invoiceprocessor.model.City;
//import com.invoiceprocessing.invoiceprocessor.model.Employee;
import com.invoiceprocessing.invoiceprocessor.model.FoodVoucherDetail;
import com.invoiceprocessing.invoiceprocessor.model.HotelVoucher;
import com.invoiceprocessing.invoiceprocessor.model.HotelVoucherDetail;
import com.invoiceprocessing.invoiceprocessor.model.State;
import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.repository.CityRepository;
import com.invoiceprocessing.invoiceprocessor.repository.FoodVoucherDetailRepository;
import com.invoiceprocessing.invoiceprocessor.repository.HotelVoucherDetailRepository;
import com.invoiceprocessing.invoiceprocessor.repository.HotelVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.StateRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TierRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherRepository;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDetailDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;

@Component("hotelVoucherMapper")
public class HotelVoucherMapper implements VoucherMapper<VoucherDto, HotelVoucher> {

	@Autowired
	private HotelVoucherDetailRepository hvDetailDto;
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private StateRepository stateRepository;
	@Autowired
	private VoucherRepository voucherrepo;
	@Autowired
	private HotelVoucherRepository hotelVoucherrepo;
	@Autowired
	private HotelVoucherDetailRepository hotelVoucherDetailRepo;
	@Autowired
	private FoodVoucherDetailRepository foodVoucherDetailRepo;
	@Autowired
	private TripRepository tripRepository;
	@Autowired
	private TierRepository tierRepository;

	@Override
	public HotelVoucher dtoToModel(VoucherDto voucher) {
		Voucher voucherEntity = new Voucher();
		HotelVoucher hotelVoucherEntity = new HotelVoucher();
		List<HotelVoucherDetail> hotelVoucherDetailList = new ArrayList<HotelVoucherDetail>();
		List<FoodVoucherDetail> foodVoucherDetailList = new ArrayList<FoodVoucherDetail>();
		List<HotelVoucher> hotelVoucherList = new ArrayList<HotelVoucher>();

		City hotelCity = cityRepository.findByCityCode(voucher.getHotelCity());
		State hotelState = stateRepository.findByStateName(voucher.getHotelState());
//		................changes made here.................
		Trip trip = null;
		if (!voucher.getTripId().equalsIgnoreCase(""))
			trip = getTripByTripId(Integer.parseInt(voucher.getTripId()));

		if (voucher != null) {
//			if (voucher.getVoucherID() == null) // changes made here.....
//				voucher.setVoucherID("");
//			if (voucher.getVoucherID().isEmpty()) {
			populateEntitiesFromDto(voucher, voucherEntity, hotelVoucherEntity, foodVoucherDetailList,
					hotelVoucherDetailList, trip, hotelCity, hotelState);
			voucherEntity = voucherrepo.save(voucherEntity);
			hotelVoucherEntity = hotelVoucherrepo.save(hotelVoucherEntity);
			hotelVoucherDetailList = hotelVoucherDetailRepo.saveAll(hotelVoucherDetailList);
			foodVoucherDetailList = foodVoucherDetailRepo.saveAll(foodVoucherDetailList);

			hotelVoucherEntity.setListOfHotelVoucherDetail(hotelVoucherDetailList);
			hotelVoucherEntity.setListOfFoodVoucherDetail(foodVoucherDetailList);

//			} else {
//
//				// 1. Update Voucher, HotelVoucher, and for each line item, HotelVoucherDetail /
//				// FoodVoucherDetail
//				// Delete existing entries in HotelVoucherDetail / FoodVoucherDetail for
//				// VoucherId
//				//
//				voucherEntity = voucherrepo.findById(Integer.parseInt(voucher.getVoucherID())).get();
//				hotelVoucherEntity = voucherEntity.getHotelVoucherID();
//				populateEntitiesFromDto(voucher, voucherEntity, hotelVoucherEntity, foodVoucherDetailList,
//						hotelVoucherDetailList, trip, hotelCity, hotelState);
//
//				// Delete
//				deleteRecordsByIdForFood(voucherEntity.getVoucherID());
//				deleteRecordsByIdForHotel(voucherEntity.getVoucherID());
//
//				// Other saves as above
//				voucherEntity = voucherrepo.save(voucherEntity);
//				hotelVoucherEntity = hotelVoucherrepo.save(hotelVoucherEntity);
//				if (!hotelVoucherDetailList.isEmpty())
//					hotelVoucherDetailRepo.saveAll(hotelVoucherDetailList);
//				if (!foodVoucherDetailList.isEmpty())
//					foodVoucherDetailRepo.saveAll(foodVoucherDetailList);
//			}
			return hotelVoucherEntity;
		} else {
			throw new NullPointerException("No Voucher Found!!");
		}
	}

	private void populateEntitiesFromDto(VoucherDto voucher, Voucher voucherEntity, HotelVoucher hotelVoucherEntity,
			List<FoodVoucherDetail> hotelVoucherDetailListForFood, List<HotelVoucherDetail> hotelVoucherDetailList,
			Trip trip, City hotelCity, State hotelState) {
		if (!voucher.getTripId().isEmpty()) // .........changes made here..........
			trip = getTripByTripId(Integer.parseInt(voucher.getTripId()));

//		.......Changes Made here 12/03/2024......
		Boolean isContains = stateRepository.findAll().stream()
				.anyMatch(voucherEntityObj -> voucherEntityObj.getState().equalsIgnoreCase(voucher.getHotelState()));

		Boolean isContainsCity = cityRepository.findAll().stream()
				.anyMatch(voucherCity -> voucherCity.getCityName().equalsIgnoreCase(voucher.getHotelCity()));

		if (isContains)
			hotelState = stateRepository.findByStateName(voucher.getHotelState());
		else {
			hotelState = new State();
			hotelState.setState(voucher.getHotelState());
			hotelState = stateRepository.save(hotelState);
		}
		if (isContainsCity)
			hotelCity = cityRepository.findByCityCode(voucher.getHotelCity());
		else {
			hotelCity = new City();
			hotelCity.setCityName(voucher.getHotelCity());
			hotelCity.setStsteID(hotelState);
			hotelCity.setTireID(tierRepository.findById(1).get());
			hotelCity = cityRepository.save(hotelCity);
		}
//		.......Changes end's here......
		HotelVoucherDetail hotelVoucherDetailEntity = null;
		FoodVoucherDetail foodVoucherDetailEntity = null;

		if (!voucher.getVoucherID().isEmpty())
			voucherEntity.setVoucherID(Integer.parseInt(voucher.getVoucherID()));
		voucherEntity.setFileName(voucher.getFilename());
		Long utilDate = new java.util.Date().getTime();
		voucherEntity.setVoucherDate(
				voucher.getDate().replace("/", " ").equalsIgnoreCase("Not Found") ? new java.sql.Date(utilDate)
						: parseStringToDate(voucher.getDate()));
		voucherEntity.setInvoiceNumber(voucher.getInvoiceNo());
		voucherEntity.setInvoiceType(voucher.getBillType());
		voucherEntity.setMerchantName(voucher.getMerchantName());
		voucherEntity.setInvoiceDate(parseStringToDate(voucher.getDate()));
		voucherEntity.setPaymentMode(voucher.getPaymentMode());
//		voucherEntity.setDeleteStatus("N");
		voucherEntity
				.setDeleteStatus(voucher.getDeleteStatus() == null || voucher.getDeleteStatus().equalsIgnoreCase("")
						|| voucher.getDeleteStatus().equalsIgnoreCase("N") ? "N" : voucher.getDeleteStatus());
		voucherEntity.setTotalAmount(new BigDecimal(voucher.getTotalAmount().replace(",", "")));
		voucherEntity.setHashText(voucher.getHashedText());
		voucherEntity.setFileImage(voucher.getImage());
		voucherEntity.setManualEntry(voucher.getManualEntry());
		voucherEntity.setTotalPax(Integer.parseInt(voucher.getPax() == null ? "0" : voucher.getPax()));
		if (voucher.getIsSingleExpense().equalsIgnoreCase("Y")) // changes made here........
			voucherEntity.setIsSingleExpense(voucher.getIsSingleExpense());
		voucherEntity.setIsSingleExpense(voucher.getIsSingleExpense());
		if (trip != null) // changes made here..........
			voucherEntity.setEmployeeID(trip.getEmployeeID());
		voucherEntity.setClaimedAmount(
				new BigDecimal(voucher.getClaimedAmount() == null ? voucher.getTotalAmount().replace(",", "")
						: voucher.getClaimedAmount().replace(",", "")));
		if (trip != null)
			voucherEntity.setTrip(trip); // .........changes made here........

		if (!voucher.getHotelVoucherID().isEmpty())
			hotelVoucherEntity.setHotelVoucherID(Integer.parseInt(voucher.getHotelVoucherID()));
		hotelVoucherEntity.setCityID(hotelCity);
		hotelVoucherEntity.setStsteID(hotelState);
		hotelVoucherEntity.setCheckInDate(parseStringToDate(voucher.getCheckinDate()));
		hotelVoucherEntity.setCheckOutDate(parseStringToDate(voucher.getCheckoutDate()));
		hotelVoucherEntity.setGuestCompanyName(voucher.getCompanyName());
		hotelVoucherEntity.setGuestCompanyGstNumber(voucher.getGstNo());
		hotelVoucherEntity.setGuestName(voucher.getGuestName());
		hotelVoucherEntity.setPinCode(voucher.getHotelPin());
		hotelVoucherEntity.setVoucherID(voucherEntity);
//		hotelVoucherEntity = hotelVoucherrepo.save(hotelVoucherEntity);

//		Voucher voucherModel = voucherrepo.findById(Integer.parseInt(voucher.getVoucherID())).get();
//		Delete the records from the database...

//		insert the records in Db
		for (VoucherDetailDto hotelVoucherDetail : voucher.getConsolidatedHotelServiceBreakage()) {

			if (StringUtils.equalsAnyIgnoreCase(hotelVoucherDetail.getServiceType(), "Food")) {
				foodVoucherDetailEntity = new FoodVoucherDetail();
				if (!hotelVoucherDetail.getDetailVoucherID().isEmpty())
					foodVoucherDetailEntity
							.setFoodVoucherDetailID(Integer.parseInt(hotelVoucherDetail.getDetailVoucherID()));
				foodVoucherDetailEntity.setHotelVoucher(hotelVoucherEntity);
				System.out.println(parseStringToDate(hotelVoucherDetail.getDate()));
				foodVoucherDetailEntity
						.setItemDate(hotelVoucherDetail.getDate().replace("/", " ").equalsIgnoreCase("not found")
								? new java.sql.Date(utilDate)
								: parseStringToDate(hotelVoucherDetail.getDate()));
				foodVoucherDetailEntity.setAmount(new BigDecimal(hotelVoucherDetail.getAmount().replace(",", "")));
				foodVoucherDetailEntity.setBillType(hotelVoucherDetail.getServiceType());
				foodVoucherDetailEntity.setDescription(hotelVoucherDetail.getDescription());
				foodVoucherDetailEntity.setCgstAmount(new BigDecimal(
						hotelVoucherDetail.getGst1().equalsIgnoreCase("") ? "0.0" : hotelVoucherDetail.getGst1()));
				foodVoucherDetailEntity.setSgstAmount(new BigDecimal(
						hotelVoucherDetail.getGst2().equalsIgnoreCase("") ? "0.0" : hotelVoucherDetail.getGst2()));
				foodVoucherDetailEntity.setGstAmount(new BigDecimal(
						hotelVoucherDetail.getGst1().replace(",", "").equalsIgnoreCase("") ? "0.0"
								: hotelVoucherDetail.getGst1())
						.add(new BigDecimal(hotelVoucherDetail.getGst2().replace(",", "").equalsIgnoreCase("") ? "0.0"
								: hotelVoucherDetail.getGst2())));
				hotelVoucherDetailListForFood.add(foodVoucherDetailEntity);
			} else {
				hotelVoucherDetailEntity = new HotelVoucherDetail();
				if (!hotelVoucherDetail.getDetailVoucherID().isEmpty())
					hotelVoucherDetailEntity
							.setHotelVoucherDetailId(Integer.parseInt(hotelVoucherDetail.getDetailVoucherID()));
				hotelVoucherDetailEntity.setAmount(new BigDecimal(hotelVoucherDetail.getAmount().replace(",", "")));
				hotelVoucherDetailEntity.setBillType(hotelVoucherDetail.getServiceType());
				hotelVoucherDetailEntity.setDate(parseStringToDate(hotelVoucherDetail.getDate()));
				hotelVoucherDetailEntity.setDescription(hotelVoucherDetail.getDescription());
				hotelVoucherDetailEntity.setSacCode(hotelVoucherDetail.getSacCode());
				hotelVoucherDetailEntity.setExceptionReason(hotelVoucherDetail.getExceptionReason());
				hotelVoucherDetailEntity.setCgstAmount(new BigDecimal(
						hotelVoucherDetail.getGst1().equalsIgnoreCase("") ? "0.0" : hotelVoucherDetail.getGst1()));
				hotelVoucherDetailEntity.setSgstAmount(new BigDecimal(
						hotelVoucherDetail.getGst2().equalsIgnoreCase("") ? "0.0" : hotelVoucherDetail.getGst2()));
				hotelVoucherDetailEntity.setGstAmount(new BigDecimal(
						hotelVoucherDetail.getGst1().replace(",", "").equalsIgnoreCase("") ? "0.0"
								: hotelVoucherDetail.getGst1())
						.add(new BigDecimal(hotelVoucherDetail.getGst2().replace(",", "").equalsIgnoreCase("") ? "0.0"
								: hotelVoucherDetail.getGst2())));
				hotelVoucherDetailEntity.setHotelVoucher(hotelVoucherEntity);
				hotelVoucherDetailList.add(hotelVoucherDetailEntity);
			}
		}

	}

	@Override
	public VoucherDto modelToDto(HotelVoucher voucherModel) {

		List<HotelVoucherDetail> hotelVoucherDetailList = hotelVoucherDetailRepo.findAll();

		VoucherDto voucherDtoForHotel = new VoucherDto();

		VoucherDto hotelVoucherDto = new VoucherDto();
		VoucherDetailDto voucherDetail = new VoucherDetailDto();
		List<HotelVoucherDetail> listOfVoucherDetailDto = hvDetailDto.findAll();
//		List<ConveyanceVoucher> listOfConveyanceVoucher = conveyanceVoucherRepository.findById(voucherModel.getHotelVoucherID());
		hotelVoucherDto.getModeOfTravel();
//		hotelVoucherDto.setCheckinDate(voucherModel.getCheckInDate());
//		hotelVoucherDto.setCheckoutDate(voucherModel.getCheckOutDate());
		hotelVoucherDto.setCompanyName(voucherModel.getGuestCompanyName());
		hotelVoucherDto.setGuestName(voucherModel.getGuestName());
		hotelVoucherDto.setHotelPin(voucherModel.getPinCode());
		for (HotelVoucherDetail hotelVoucherDetail : listOfVoucherDetailDto) {
//			voucherDetail.setAmount(hotelVoucherDetail.getAmount());
//			voucherDetail.setDate(hotelVoucherDetail.getDate());
			voucherDetail.setDescription(hotelVoucherDetail.getDescription());
			voucherDetail.setExceptionReason(hotelVoucherDetail.getExceptionReason());
			voucherDetail.setSacCode(hotelVoucherDetail.getSacCode());
			voucherDetail.setServiceType(hotelVoucherDetail.getBillType());
		}

		return hotelVoucherDto;
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

	private Trip getTripByTripId(Integer tripId) {
		Optional<Trip> tripEntity = tripRepository.findById(tripId);
		return tripEntity.get();
	}

	private void deleteRecordsByIdForHotel(Integer voucherId) {

		// Db informations....
		String url = "jdbc:mysql://localhost:3306/expendesk";
		String user = "root";
		String password = "#Sroy@1010";

		String query = "delete from hotel_voucher_detail where HOTEL_VOUCHER_ID = \r\n"
				+ "(select HOTEL_VOUCHER_ID from hotel_voucher where VOUCHER_ID = ?)";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, voucherId);
			int affected = preparedStatement.executeUpdate();
			System.out.println("affected" + affected);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void deleteRecordsByIdForFood(Integer voucherId) {

		// Db informations....
		String url = "jdbc:mysql://localhost:3306/expendesk";
		String user = "root";
		String password = "#Sroy@1010";

		String query = "delete from food_voucher_detail where HOTEL_VOUCHER_ID = \r\n"
				+ "(select HOTEL_VOUCHER_ID from hotel_voucher where VOUCHER_ID = ?)";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, voucherId);
			int affected = preparedStatement.executeUpdate();
			System.out.println("affected" + affected);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
