package com.invoiceprocessing.invoiceprocessor.service;

import java.sql.Time;
//import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.mapper.TripMapper;
import com.invoiceprocessing.invoiceprocessor.model.ConveyanceVoucher;
import com.invoiceprocessing.invoiceprocessor.model.CostCode;
import com.invoiceprocessing.invoiceprocessor.model.FoodVoucher;
import com.invoiceprocessing.invoiceprocessor.model.FoodVoucherDetail;
import com.invoiceprocessing.invoiceprocessor.model.HotelVoucher;
import com.invoiceprocessing.invoiceprocessor.model.HotelVoucherDetail;
import com.invoiceprocessing.invoiceprocessor.model.Project;
import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.model.TripCosts;
import com.invoiceprocessing.invoiceprocessor.model.TripProjects;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.repository.ConveyanceVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.CostCodeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.FoodVoucherDetailRepository;
import com.invoiceprocessing.invoiceprocessor.repository.FoodVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.HotelVoucherDetailRepository;
import com.invoiceprocessing.invoiceprocessor.repository.HotelVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.ProjectRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripCostsRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripProjectsRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherRepository;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;
import com.invoiceprocessing.invoiceprocessor.response.TravelTripCreationDto;
import com.invoiceprocessing.invoiceprocessor.response.TripDTO;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDetailDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.TripService;
import com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants;

//import jakarta.persistence.EntityManager;

//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.Persistence;
//import jakarta.persistence.PersistenceContext;

@Service
public class TripServiceImpl implements TripService {

	private static final String TRIP_CONTEXT = "trip";

//	@Autowired
//	private EntityManager entityManager;

	@Autowired
	TripProjectsRepository tripProjectsRepository;

	@Autowired
	TripCostsRepository tripCostsRepository;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	CostCodeRepository costCodeRepository;

	@Autowired
	TripRepository tripRepository;

	@Autowired
	HotelVoucherDetailRepository hotelVoucherDetailRepository;

	@Autowired
	FoodVoucherDetailRepository foodVoucherDetailRepository;

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	FoodVoucherRepository foodVoucherRepository;

	@Autowired
	ConveyanceVoucherRepository conveyanceVoucherRepository;

	@Autowired
	HotelVoucherRepository hotelVoucherRepository;

	@Override
	public Trip saveAndGenerateTripID(TripDTO tripInfo) {
		@SuppressWarnings("unchecked")
		TripMapper<TripDTO, Trip> tripMapper = (TripMapper<TripDTO, Trip>) applicationContext.getBean(TRIP_CONTEXT);
		return tripMapper.dtoToModel(tripInfo);
	}

	@Override
	public TripDTO fetchEmployeeById(Integer tripId) {
//		Optional<Trip> trip = tripRepository.findById(tripId);
		TripDTO tripDto = new TripDTO();

		Trip trip = tripRepository.findById(tripId).get();
		tripDto.setEmployeeID(trip.getEmployeeID().getEmployeeId().toString());
		tripDto.setFromDate(trip.getFromDate().toString());
		tripDto.setToDate(trip.getToDate().toString());
		tripDto.setExpenseStatus(trip.getExpenseStatus());
		tripDto.setTripName(trip.getTripName());
		return tripDto;
	}

	@Override
	public List<VoucherDto> getAllTripInfo(Integer tripId) {

		List<VoucherDto> listOfVoucherDto = new ArrayList<VoucherDto>();
//		List<HotelVoucherDetail> hotelVoucherDetailList = new ArrayList<HotelVoucherDetail>();
//		List<FoodVoucherDetail> foodVoucherDetailList = new ArrayList<FoodVoucherDetail>();

		Trip trip = tripRepository.findById(tripId).get();

		// Map Entity to DTO
		List<Voucher> vouchers = trip.getVouchers();

		VoucherDto voucherDto = new VoucherDto();
		VoucherDetailDto voucherDetailDto;
//		voucherDto.setTripId(trip.getTripID().toString());
		// Map Trip

//		voucherDto.setFromLocation(trip);

		for (Voucher voucher : vouchers) {

//			check if voucher has not any delete status "Y" then simply discard it
			if (voucher != null && voucher.getDeleteStatus() != null
					&& voucher.getDeleteStatus().equalsIgnoreCase("N")) {

				// Common attributes
				voucherDto = new VoucherDto();
				voucherDto.setVoucherID(voucher.getVoucherID().toString());
				voucherDto.setTripId(trip.getTripID().toString());
				voucherDto.setBillType(voucher.getInvoiceType());
				voucherDto.setClaimedAmount(voucher.getClaimedAmount().toString());
				voucherDto.setFilename(voucher.getFileName());
				voucherDto.setHashedText(voucher.getHashText());
				voucherDto.setDeleteStatus(voucher.getDeleteStatus());
				voucherDto.setInvoiceNo(voucher.getInvoiceNumber());
				voucherDto.setImage(voucher.getFileImage());
				voucherDto.setDate(
						voucher.getVoucherDate() == null ? new SimpleDateFormat("dd-MM-yyyy").format(new Date())
								: new SimpleDateFormat("dd-MM-yyyy").format(voucher.getVoucherDate()));
				voucherDto.setMerchantName(voucher.getMerchantName());
				voucherDto
						.setTotalAmount(voucher.getTotalAmount() == null ? "0.0" : voucher.getTotalAmount().toString());
				voucherDto.setExceptionReason(voucher.getExceptionReason());
				voucherDto.setManualEntry(voucher.getManualEntry());
				voucherDto.setPax(voucher.getTotalPax() == null ? "1" : voucher.getTotalPax().toString());
				voucherDto.setPaymentMode(voucher.getPaymentMode());

//			validated fields like date, GST etc.
//			voucherDto.setCheckinDateFormat(1);
//			voucherDto.setCheckoutDateFormat(1);
//			voucherDto.setDateFormat(1);
//			voucherDto.setGstFormat("1");

				// Hotel attributes
				if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.HOTEL_TYPE)) {
					if (voucher.getHotelVoucherID() != null) {
						voucherDto.setHotelVoucherID(voucher.getHotelVoucherID().getHotelVoucherID().toString());
						voucherDto.setCompanyName(voucher.getHotelVoucherID().getGuestCompanyName());
						voucherDto.setGuestName(voucher.getHotelVoucherID().getGuestName());
						voucherDto.setHotelPin(voucher.getHotelVoucherID().getPinCode());
						voucherDto.setGstNo(voucher.getHotelVoucherID().getGuestCompanyGstNumber()); // ..... change is
																										// made
																										// here
																										// ....(In March
																										// (12/03/2024))
						voucherDto.setHotelCity(voucher.getHotelVoucherID().getCityID().getCityName());
						voucherDto.setHotelState(voucher.getHotelVoucherID().getStsteID().getState());
						voucherDto.setCheckinDate(new SimpleDateFormat("dd-MM-yyyy")
								.format(voucher.getHotelVoucherID().getCheckInDate()));
						voucherDto.setCheckoutDate(new SimpleDateFormat("dd-MM-yyyy")
								.format(voucher.getHotelVoucherID().getCheckOutDate()));
						voucherDto.setAccommodationType(voucher.getHotelVoucherID().getAccomodationCategory());

						List<VoucherDetailDto> consolidatedHotelServiceBreakage = new ArrayList<VoucherDetailDto>();
						for (HotelVoucherDetail hotelVoucherDetail : voucher.getHotelVoucherID()
								.getListOfHotelVoucherDetail()) {
							voucherDetailDto = new VoucherDetailDto();
							voucherDetailDto
									.setDetailVoucherID(hotelVoucherDetail.getHotelVoucherDetailId().toString());
							voucherDetailDto.setAmount(hotelVoucherDetail.getAmount() == null ? "0.0"
									: hotelVoucherDetail.getAmount().toString());
							voucherDetailDto
									.setDate(new SimpleDateFormat("dd-MM-yyyy").format(hotelVoucherDetail.getDate()));
							voucherDetailDto.setDescription(hotelVoucherDetail.getDescription());
							voucherDetailDto.setGst1(hotelVoucherDetail.getCgstAmount().toString());
							voucherDetailDto.setGst2(hotelVoucherDetail.getSgstAmount().toString());
//					voucherDetailDto.setGst1(hotelVoucherDetail.getGstAmount() == null ? "0.0"
//							: hotelVoucherDetail.getGstAmount().toString());
							voucherDetailDto.setSacCode(hotelVoucherDetail.getSacCode());
							voucherDetailDto.setServiceType(hotelVoucherDetail.getBillType());
							consolidatedHotelServiceBreakage.add(voucherDetailDto);
						}

						for (FoodVoucherDetail foodVoucherDetail : voucher.getHotelVoucherID()
								.getListOfFoodVoucherDetail()) {
							voucherDetailDto = new VoucherDetailDto();
							voucherDetailDto.setDetailVoucherID(foodVoucherDetail.getFoodVoucherDetailID().toString());
							voucherDetailDto.setAmount(foodVoucherDetail.getAmount() == null ? "0.0"
									: foodVoucherDetail.getAmount().toString());
							voucherDetailDto.setServiceType(foodVoucherDetail.getBillType());
							voucherDetailDto.setDescription(foodVoucherDetail.getDescription());
							voucherDetailDto.setGst1(foodVoucherDetail.getCgstAmount().toString());
							voucherDetailDto.setGst2(foodVoucherDetail.getSgstAmount().toString());
//					voucherDetailDto.setGst1(foodVoucherDetail.getGstAmount() == null ? "0.0"
//							: foodVoucherDetail.getGstAmount().toString());
							voucherDetailDto.setDate(
									new SimpleDateFormat("dd-MM-yyyy").format(foodVoucherDetail.getItemDate()));
							consolidatedHotelServiceBreakage.add(voucherDetailDto);
						}

						consolidatedHotelServiceBreakage.sort((a, b) -> a.getDate().compareTo(b.getDate()));
						voucherDto.setConsolidatedHotelServiceBreakage(consolidatedHotelServiceBreakage);
					}
					// Food attributes
				} else if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)) {
					if (voucher.getFoodVoucherID() != null) {
						if (voucher.getManualEntry().equalsIgnoreCase("N"))
							voucherDto.setFoodVoucherID(voucher.getFoodVoucherID().getFoodVoucherID().toString());
						voucherDto.setGstNo(voucher.getFoodVoucherID() == null ? "0.0"
								: voucher.getFoodVoucherID().getGstAmount().toString());
//				voucherDto.setClaimedAmount(voucher.getFoodVoucherID().getClaimedAmount().toString());
//				voucherDto.setTotalAmount(voucher.getFoodVoucherID().getTotalAmount().toString());
						voucherDto.setLiquorStatus(voucher.getFoodVoucherID() == null ? "false"
								: voucher.getFoodVoucherID().getContainsLiquor().toString());
						voucherDto.setCgstAmount(voucher.getFoodVoucherID() == null ? "0.0"
								: voucher.getFoodVoucherID().getCgstAmount());
						voucherDto.setSgstAmount(voucher.getFoodVoucherID() == null ? "0.0"
								: voucher.getFoodVoucherID().getSgstAmount());
					}
				} else if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)) {
					if (voucher.getConveyanceVoucherID() != null) {
						// Conveyance attributes
						if (voucher.getManualEntry().equalsIgnoreCase("N"))
							voucherDto.setConveyanceVoucherID(
									voucher.getConveyanceVoucherID().getConveyanceVoucherID().toString());
						voucherDto.setNoOfKm(voucher.getConveyanceVoucherID() == null ? "0.0"
								: voucher.getConveyanceVoucherID().getDistance().toString());
						voucherDto.setFromLocation(voucher.getConveyanceVoucherID() == null ? "N/A"
								: voucher.getConveyanceVoucherID().getFromLocation());
						voucherDto.setModeOfTravel(voucher.getConveyanceVoucherID() == null ? "N/A"
								: voucher.getConveyanceVoucherID().getModeOfTravel());
						voucherDto.setToLocation(voucher.getConveyanceVoucherID() == null ? "N/A"
								: voucher.getConveyanceVoucherID().getToLocation());
						voucherDto.setTravelTicketClass(voucher.getConveyanceVoucherID() == null ? "N/A"
								: voucher.getConveyanceVoucherID().getTravelClass());
						voucherDto.setArrivalDate(voucher.getConveyanceVoucherID() == null ? "N/A"
								: voucher.getConveyanceVoucherID().getArrivalDate() == null
										? new SimpleDateFormat("dd-MM-yyyy").format(new Date())
										: new SimpleDateFormat("dd-MM-yyyy")
												.format(voucher.getConveyanceVoucherID().getArrivalDate()));
						voucherDto.setArrivalTime(voucher.getConveyanceVoucherID() == null ? "N/A"
								: voucher.getConveyanceVoucherID().getArrivalTime() == null
										? new SimpleDateFormat("hh:mm:ss a").format(new Time(0))
										: voucher.getConveyanceVoucherID().getArrivalTime());
						voucherDto.setDepartureTime(voucher.getConveyanceVoucherID() == null ? "N/A"
								: voucher.getConveyanceVoucherID().getDepartureTime() == null
										? new SimpleDateFormat("hh:mm:ss a").format(new Time(0))
										: voucher.getConveyanceVoucherID().getDepartureTime());
						voucherDto.setDepartureDate(voucher.getConveyanceVoucherID() == null ? "N/A"
								: voucher.getConveyanceVoucherID().getDepartureDate() == null
										? new SimpleDateFormat("dd-MM-yyyy").format(new Date())
										: new SimpleDateFormat("dd-MM-yyyy")
												.format(voucher.getConveyanceVoucherID().getDepartureDate()));
						voucherDto.setIntraOrInterCityTravel(voucher.getConveyanceVoucherID() == null ? "N/A"
								: voucher.getConveyanceVoucherID().getInterIntraCity());
						voucherDto.setModeOfTravel(voucher.getConveyanceVoucherID().getModeOfTravel());
						voucherDto.setCabType(voucher.getConveyanceVoucherID().getCabType());
					}
				}
				listOfVoucherDto.add(voucherDto);
			}
		}
		return listOfVoucherDto;
	}

	@Override
	public List<VoucherDto> getIndividualVoucherRecord(VoucherDto voucherInfo) {
		// TODO Auto-generated method stub
		Trip trip = tripRepository.findById(Integer.parseInt(voucherInfo.getTripId())).get();
		List<Voucher> listOfVouchersUnderTrip = voucherRepository.findByTrip(trip);
		List<VoucherDto> listOfVoucherDtos = new ArrayList<VoucherDto>();
		VoucherDto voucherDto = null;
		VoucherDetailDto voucherDetailDto = null;

		for (Voucher voucher : listOfVouchersUnderTrip) {
			if (voucher.getDeleteStatus().equalsIgnoreCase("N")) {
				if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)
						&& (new SimpleDateFormat("dd-MM-yyyy").format(voucher.getVoucherDate())
								.equalsIgnoreCase(voucherInfo.getDate()))) {

					voucherDto = new VoucherDto();
					voucherDto.setBillType(voucher.getInvoiceType());
					voucherDto.setFilename(voucher.getFileName());
					voucherDto.setMerchantName(voucher.getMerchantName());
					voucherDto.setVoucherID(voucher.getVoucherID().toString());
					voucherDto.setClaimedAmount(voucher.getClaimedAmount().toString());
					voucherDto.setTripId(voucher.getTrip().getTripID().toString());
					voucherDto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(voucher.getVoucherDate()));
					voucherDto.setInvoiceNo(voucher.getInvoiceNumber());
					voucherDto.setManualEntry(voucher.getManualEntry());
					voucherDto.setHashedText(voucher.getHashText());
					voucherDto.setIsApproved(voucher.getIsApproved());
					voucherDto.setImage(voucher.getFileImage());
					getAllDetailForCorrespondingFoodVoucher(voucherDto, voucher);
					listOfVoucherDtos.add(voucherDto);

				}

				if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)
						&& (new SimpleDateFormat("dd-MM-yyyy").format(voucher.getVoucherDate())
								.equalsIgnoreCase(voucherInfo.getDate()))) {

					voucherDto = new VoucherDto();
					voucherDto.setBillType(voucher.getInvoiceType());
					voucherDto.setFilename(voucher.getFileName());
					voucherDto.setMerchantName(voucher.getMerchantName());
					voucherDto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(voucher.getVoucherDate()));
					voucherDto.setVoucherID(voucher.getVoucherID().toString());
					voucherDto.setClaimedAmount(voucher.getClaimedAmount().toString());
					voucherDto.setTotalAmount(voucher.getTotalAmount().toString());
					voucherDto.setTripId(voucher.getTrip().getTripID().toString());
					voucherDto.setInvoiceNo(voucher.getInvoiceNumber());
					voucherDto.setHashedText(voucher.getHashText());
					voucherDto.setManualEntry(voucher.getManualEntry());
					voucherDto.setImage(voucher.getFileImage());
					getAllDetailForCorrespondingConveyanceVoucher(voucherDto, voucher);
					listOfVoucherDtos.add(voucherDto);

				}

				if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.HOTEL_TYPE)
						|| voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.ACCOMMODATION_TYPE)) {
					// For Hotel fetch the information of the hotels and hotel voucher Details and
					// For Food Food voucher Details
					HotelVoucher hotelVoucher = hotelVoucherRepository.findByVoucherID(voucher);
					List<HotelVoucherDetail> listOfHotelVoucherDetail = hotelVoucherDetailRepository
							.findByHotelVoucherID(hotelVoucher);
					List<FoodVoucherDetail> listOfFoodVoucherDetail = foodVoucherDetailRepository
							.findByHotelVoucher(hotelVoucher);
					List<VoucherDetailDto> listOfVoucherDetailDtos = new ArrayList<VoucherDetailDto>();

					Boolean flagIfMatchHotelVoucherItemDate = listOfHotelVoucherDetail.stream()
							.anyMatch(detail -> new SimpleDateFormat("dd-MM-yyyy").format(detail.getDate())
									.equalsIgnoreCase(voucherInfo.getDate()));

					Boolean flagIfMatchFoodVoucherItemDate = listOfFoodVoucherDetail.stream()
							.anyMatch(detailVoucher -> new SimpleDateFormat("dd-MM-yyyy")
									.format(detailVoucher.getItemDate()).equalsIgnoreCase(voucherInfo.getDate()));

					if (flagIfMatchHotelVoucherItemDate || flagIfMatchFoodVoucherItemDate) {
						for (HotelVoucherDetail hotelVoucherDetail : listOfHotelVoucherDetail) {

							voucherDetailDto = new VoucherDetailDto();
							voucherDetailDto
									.setDetailVoucherID(hotelVoucherDetail.getHotelVoucherDetailId().toString());
							voucherDetailDto.setAmount(hotelVoucherDetail.getAmount().toString());
							voucherDetailDto
									.setDate(new SimpleDateFormat("dd-MM-yyyy").format(hotelVoucherDetail.getDate()));
							voucherDetailDto.setDescription(hotelVoucherDetail.getDescription());
							voucherDetailDto.setGst1(hotelVoucherDetail.getCgstAmount().toString());
							voucherDetailDto.setGst2(hotelVoucherDetail.getSgstAmount().toString());
							voucherDetailDto.setSacCode(hotelVoucherDetail.getSacCode());
							voucherDetailDto.setServiceType(hotelVoucherDetail.getBillType());

							listOfVoucherDetailDtos.add(voucherDetailDto);

						}

						for (FoodVoucherDetail foodVoucherDetail : listOfFoodVoucherDetail) {

							voucherDetailDto = new VoucherDetailDto();
							voucherDetailDto.setDetailVoucherID(foodVoucherDetail.getFoodVoucherDetailID().toString());
							voucherDetailDto.setAmount(foodVoucherDetail.getAmount().toString());
							voucherDetailDto.setServiceType(foodVoucherDetail.getBillType());
							voucherDetailDto.setDescription(foodVoucherDetail.getDescription());
							voucherDetailDto.setDate(
									new SimpleDateFormat("dd-MM-yyyy").format(foodVoucherDetail.getItemDate()));
							voucherDetailDto.setGst1(foodVoucherDetail.getCgstAmount().toString());
							voucherDetailDto.setGst2(foodVoucherDetail.getSgstAmount().toString());

							listOfVoucherDetailDtos.add(voucherDetailDto);

						}
					}

					if (!listOfVoucherDetailDtos.isEmpty()) {

						voucherDto = new VoucherDto();
						voucherDto.setVoucherID(voucher.getVoucherID().toString());
						voucherDto.setBillType(voucher.getInvoiceType());
						voucherDto.setFilename(voucher.getFileName());
						voucherDto.setTotalAmount(voucher.getTotalAmount().toString());
						voucherDto.setMerchantName(voucher.getMerchantName());
						voucherDto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(voucher.getVoucherDate()));
						voucherDto.setClaimedAmount(voucher.getClaimedAmount().toString());
						voucherDto.setTripId(voucher.getTrip().getTripID().toString());
						voucherDto.setInvoiceNo(voucher.getInvoiceNumber());
						voucherDto.setHashedText(voucher.getHashText());
						voucherDto.setIsApproved(voucher.getIsApproved());
						voucherDto.setManualEntry(voucher.getManualEntry());
						voucherDto.setImage(voucher.getFileImage());
						voucherDto.setConsolidatedHotelServiceBreakage(listOfVoucherDetailDtos);
						voucherDto.setGuestName(hotelVoucher.getGuestName());
						voucherDto.setHotelState(hotelVoucher.getStsteID().getState());
						voucherDto.setHotelCity(hotelVoucher.getCityID().getCityName());
						voucherDto.setHotelPin(hotelVoucher.getPinCode());
						voucherDto.setCompanyName(hotelVoucher.getGuestCompanyName());
						voucherDto.setGstNo(hotelVoucher.getGuestCompanyGstNumber());
						voucherDto.setCheckinDate(
								new SimpleDateFormat("dd-MM-yyyy").format(hotelVoucher.getCheckInDate()));
						voucherDto.setCheckoutDate(
								new SimpleDateFormat("dd-MM-yyyy").format(hotelVoucher.getCheckOutDate()));
						voucherDto.setPax(voucher.getTotalPax() == null ? "0" : voucher.getTotalPax().toString());

						listOfVoucherDtos.add(voucherDto);
					}
				}

			}
		}
		return listOfVoucherDtos;
	}

	private void getAllDetailForCorrespondingConveyanceVoucher(VoucherDto voucherDto, Voucher voucher) {
		// TODO Auto-generated method stub
		List<ConveyanceVoucher> listOfConveyanceVouchers = conveyanceVoucherRepository.findByVoucherID(voucher);
		for (ConveyanceVoucher conveyanceVoucher : listOfConveyanceVouchers) {
			voucherDto.setConveyanceVoucherID(conveyanceVoucher.getConveyanceVoucherID().toString());
			voucherDto.setArrivalDate(
					conveyanceVoucher.getArrivalDate() == null ? new SimpleDateFormat("dd-MM-yyyy").format(new Date())
							: new SimpleDateFormat("dd-MM-yyyy").format(conveyanceVoucher.getArrivalDate()));
//			voucherDto.setArrivalTime(conveyanceVoucher.getArrivalTime() == null
//					? Time.valueOf(LocalTime.now()).toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
//					: conveyanceVoucher.getArrivalTime().toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
			voucherDto.setDepartureDate(
					conveyanceVoucher.getDepartureDate() == null ? new SimpleDateFormat("dd-MM-yyyy").format(new Date())
							: new SimpleDateFormat("dd-MM-yyyy").format(conveyanceVoucher.getDepartureDate()));
//			voucherDto.setDepartureTime(conveyanceVoucher.getDepartureTime() == null
//					? Time.valueOf(LocalTime.now()).toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
//					: conveyanceVoucher.getDepartureTime().toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
			voucherDto.setArrivalTime(
					conveyanceVoucher.getArrivalTime() != null ? conveyanceVoucher.getArrivalTime() : "");
			voucherDto.setDepartureTime(
					conveyanceVoucher.getDepartureTime() != null ? conveyanceVoucher.getDepartureTime() : "");
			voucherDto.setFromLocation(conveyanceVoucher.getFromLocation());
			voucherDto.setToLocation(conveyanceVoucher.getToLocation());
			voucherDto.setNoOfKm(conveyanceVoucher.getDistance().toString());
			voucherDto.setModeOfTravel(conveyanceVoucher.getModeOfTravel());
			if (voucher.getManualEntry().equalsIgnoreCase("Y"))
				voucherDto.setCabType(conveyanceVoucher.getCabType());
			voucherDto.setTravelTicketClass(conveyanceVoucher.getTravelClass());
			voucherDto.setInterIntraCity(conveyanceVoucher.getInterIntraCity());
			voucherDto.setConveyanceVoucherID(conveyanceVoucher.getConveyanceVoucherID().toString());
		}

	}

	private void getAllDetailForCorrespondingFoodVoucher(VoucherDto voucherDto, Voucher voucher) {
		// TODO Auto-generated method stub
		List<FoodVoucher> listOfFoodVouchers = foodVoucherRepository.findByVoucherID(voucher);
		for (FoodVoucher foodVoucher : listOfFoodVouchers) {
			voucherDto.setFoodVoucherID(foodVoucher.getFoodVoucherID().toString());
			voucherDto.setClaimedAmount(foodVoucher.getClaimedAmount().toString());
			voucherDto.setObjectionRaise(foodVoucher.getObjectionRaise());
			voucherDto.setLiquorStatus(foodVoucher.getContainsLiquor().toString());
			voucherDto.setObjectionRaise(foodVoucher.getObjectionRaise());
			voucherDto.setTotalAmount(foodVoucher.getTotalAmount().toString());
			voucherDto.setVoucherID(foodVoucher.getVoucherID().getVoucherID().toString());
			voucherDto.setCgstAmount(foodVoucher.getCgstAmount());
			voucherDto.setSgstAmount(foodVoucher.getSgstAmount());

		}
	}

	@Override
	public ResponseMassageWithCodeDto updateTravelDetails(TravelTripCreationDto travelTripCreationDto) {
		// TODO Auto-generated method stub

		TripProjects tripProjects = null;
		Project project = new Project();
		TripCosts tripCosts = new TripCosts();
		CostCode costCodeEntity = new CostCode();
		Trip trip = tripRepository.findById(travelTripCreationDto.getTripId()).get();
		ResponseMassageWithCodeDto responseMassageWithCodeDto = new ResponseMassageWithCodeDto();
		List<TripProjects> listOfTripProject = new ArrayList<TripProjects>();
		List<TripCosts> listOfTripCosts = new ArrayList<TripCosts>();

		trip.setExpenseStatus("D");

//		update trip
		tripRepository.save(trip);

		responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
		responseMassageWithCodeDto.setMassage("Updated!!");

//		save data of project and cost code (For a Trip)

		if (!travelTripCreationDto.getProjectCodes().isEmpty()) {
			for (String projectCode : travelTripCreationDto.getProjectCodes()) {

				tripProjects = new TripProjects();
				tripProjects.setTripId(trip);
				project = projectRepository.findByProjectCode(projectCode);
				tripProjects.setProjectId(project);

				listOfTripProject.add(tripProjects);

			}
			tripProjectsRepository.saveAll(listOfTripProject);
		}

		if (!travelTripCreationDto.getCostCodes().isEmpty()) {
			for (String costCode : travelTripCreationDto.getCostCodes()) {

				tripCosts = new TripCosts();
				tripCosts.setTripId(trip);
				costCodeEntity = costCodeRepository.findByCostCode(costCode);
				tripCosts.setCostId(costCodeEntity);

				listOfTripCosts.add(tripCosts);

			}
			tripCostsRepository.saveAll(listOfTripCosts);
		}
		return responseMassageWithCodeDto;
	}

	@Override
	public ResponseMassageWithCodeDto deleteTripById(VoucherDto voucherDto) {
		// TODO Auto-generated method stub

		ResponseMassageWithCodeDto responseMassageWithCodeDto = new ResponseMassageWithCodeDto();

		Trip trip = tripRepository.findById(Integer.parseInt(voucherDto.getTripId())).orElse(null);

		if (trip != null) {
			trip.setDeleteStatus(voucherDto.getDeleteStatus());
			tripRepository.save(trip);
		}
		responseMassageWithCodeDto.setMassage("update trip successfully!!!");
		responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
		return responseMassageWithCodeDto;
	}
}
