package com.invoiceprocessing.invoiceprocessor.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.mapper.ConsolidatedHotelServiceBreakageMapper;
import com.invoiceprocessing.invoiceprocessor.mapper.VoucherMapper;
import com.invoiceprocessing.invoiceprocessor.model.HotelVoucher;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.model.VoucherEntity;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherRepository;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDetailDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.VoucherService;
import com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants;

@Service
public class VoucherServiceImpl implements VoucherService {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	ConsolidatedHotelServiceBreakageMapper consolidatedHotelServiceBreakageMapper;

	@Override
	public VoucherEntity saveHotelVoucher(VoucherDto voucherDto) {
		@SuppressWarnings("unchecked")
		VoucherMapper<VoucherDto, VoucherEntity> voucherMapper = (VoucherMapper<VoucherDto, VoucherEntity>) applicationContext
				.getBean(resolveVoucherMapper(voucherDto.getBillType()));
		return voucherMapper.dtoToModel(voucherDto);
	}

	private String resolveVoucherMapper(String voucherType) {
		return voucherType + "VoucherMapper";
	}

//	For Food Voucher Service implementation...

	@Override
	public VoucherEntity saveFoodVoucher(VoucherDto foodVoucherDto) {
		@SuppressWarnings("unchecked")
		VoucherMapper<VoucherDto, VoucherEntity> voucherMapperForFood = (VoucherMapper<VoucherDto, VoucherEntity>) applicationContext
				.getBean(resolveVoucherMapper(foodVoucherDto.getBillType()));
		return voucherMapperForFood.dtoToModel(foodVoucherDto);
	}

//	For Conveyance Voucher Service implementation...

	@Override
	public VoucherEntity saveConveyanceVoucher(VoucherDto conveyanceVoucherDto) {
		@SuppressWarnings("unchecked")
		VoucherMapper<VoucherDto, VoucherEntity> voucherMapper = (VoucherMapper<VoucherDto, VoucherEntity>) applicationContext
				.getBean(resolveVoucherMapper(conveyanceVoucherDto.getBillType()));
		return voucherMapper.dtoToModel(conveyanceVoucherDto);

	}

	@Override
	public List<VoucherWithOutTripDto> getVoucherInfoByVoucherId(VoucherWithOutTripDto voucherWithOutTripDto) {
		// TODO Auto-generated method stub

		Voucher voucher = voucherRepository.findById(Integer.parseInt(voucherWithOutTripDto.getVoucherID()))
				.orElse(null);

		VoucherWithOutTripDto voucherWithOutTrip = new VoucherWithOutTripDto();

		List<VoucherWithOutTripDto> listOfVoucherWithoutTrip = new ArrayList<VoucherWithOutTripDto>();

		if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)) {
			insertDataInDto(voucherWithOutTrip, voucher);
			listOfVoucherWithoutTrip.add(voucherWithOutTrip);
		} else if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)) {
			insertDataInDto(voucherWithOutTrip, voucher);
			listOfVoucherWithoutTrip.add(voucherWithOutTrip);
		}

		return listOfVoucherWithoutTrip;
	}

	private void insertDataInDto(VoucherWithOutTripDto voucherWithOutTrip, Voucher voucher) {
		// TODO Auto-generated method stub

//		for common fields for every voucher (Header Section for voucher)
		voucherWithOutTrip.setVoucherID(voucher.getVoucherID().toString());
		voucherWithOutTrip.setClaimedAmount(voucher.getClaimedAmount().toString());
		voucherWithOutTrip.setExceptionReason(voucher.getExceptionReason());
		voucherWithOutTrip.setImage(voucher.getFileImage());
		voucherWithOutTrip.setFilename(voucher.getFileName());
		voucherWithOutTrip.setHashedText(voucher.getHashText());
		voucherWithOutTrip.setIsApproved(voucher.getIsApproved());
		voucherWithOutTrip.setDate(new SimpleDateFormat("dd-MM-yyyy").format(voucher.getVoucherDate()));
		voucherWithOutTrip.setInvoiceNo(voucher.getInvoiceNumber());
		voucherWithOutTrip.setBillType(voucher.getInvoiceType());
		voucherWithOutTrip.setMerchantName(voucher.getMerchantName());
		voucherWithOutTrip.setTotalAmount(voucher.getTotalAmount().toString());
		voucherWithOutTrip.setEmployeeId(voucher.getEmployeeID().getEmployeeId());
		voucherWithOutTrip.setManualEntry(voucher.getManualEntry());
		voucherWithOutTrip.setPax(voucher.getTotalPax() != null ? voucher.getTotalPax().toString() : "");
		voucherWithOutTrip.setIsSingleExpense(voucher.getIsSingleExpense());
		voucherWithOutTrip.setCcTransactionId(voucher.getCreditCardTransactions() != null
				? voucher.getCreditCardTransactions().getCcTransactionId().toString()
				: "");

//		For food now we can put the details of the food voucher
		if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)
				&& (voucher.getFoodVoucherID() != null)) {
			voucherWithOutTrip.setFoodVoucherId(voucher.getFoodVoucherID().getFoodVoucherID().toString());
			voucherWithOutTrip.setTotalAmount(voucher.getFoodVoucherID().getTotalAmount().toString());
			voucherWithOutTrip.setVoucherID(voucher.getVoucherID().toString());
			voucherWithOutTrip.setCgstAmount(voucher.getFoodVoucherID().getCgstAmount());
			voucherWithOutTrip.setSgstAmount(voucher.getFoodVoucherID().getSgstAmount());
			voucherWithOutTrip.setObjectionRaise(voucher.getFoodVoucherID().getObjectionRaise());
			voucherWithOutTrip.setLiquorStatus(voucher.getFoodVoucherID() == null ? "false"
					: voucher.getFoodVoucherID().getContainsLiquor().toString());

		}

//		For Conveyance voucher we put the details of the conveyance voucher
		if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)
				&& (voucher.getConveyanceVoucherID() != null)) {
			voucherWithOutTrip
					.setConveyanceVoucherID(voucher.getConveyanceVoucherID().getConveyanceVoucherID().toString());
			voucherWithOutTrip.setNoOfKm(voucher.getConveyanceVoucherID().getDistance().toString());
			voucherWithOutTrip.setFromLocation(voucher.getConveyanceVoucherID().getFromLocation());
			voucherWithOutTrip.setToLocation(voucher.getConveyanceVoucherID().getToLocation());
			voucherWithOutTrip.setModeOfTravel(voucher.getConveyanceVoucherID().getModeOfTravel());
			voucherWithOutTrip.setCabType(voucher.getConveyanceVoucherID().getCabType());
			voucherWithOutTrip.setTravelClass(voucher.getConveyanceVoucherID().getTravelClass());
			voucherWithOutTrip.setVehicleType(voucher.getConveyanceVoucherID().getVehicleType());
			voucherWithOutTrip.setVehicleFuelType(voucher.getConveyanceVoucherID().getVehicleFuelType());
			voucherWithOutTrip.setIntraOrInterCityTravel(voucher.getConveyanceVoucherID().getInterIntraCity());
			voucherWithOutTrip.setDepartureDate(
					new SimpleDateFormat("dd-MM-yyyy").format(voucher.getConveyanceVoucherID().getDepartureDate()));
			voucherWithOutTrip.setArrivalDate(
					new SimpleDateFormat("dd-MM-yyyy").format(voucher.getConveyanceVoucherID().getArrivalDate()));
			voucherWithOutTrip.setArrivalTime(voucher.getConveyanceVoucherID().getArrivalTime());
			voucherWithOutTrip.setDepartureTime(voucher.getConveyanceVoucherID().getDepartureTime());
//			voucherWithOutTrip.setArrivalTime(voucher.getConveyanceVoucherID().getArrivalTime().toLocalTime()
//					.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
//			voucherWithOutTrip.setDepartureTime(voucher.getConveyanceVoucherID().getDepartureTime().toLocalTime()
//					.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		}

	}

	@Override
	public ResponseMassageWithCodeDto deleteVoucherWithVoucherId(List<VoucherDto> listVoucherDtos) {
		// TODO Auto-generated method stub
		Voucher voucher = null;
		List<Voucher> listOfVouchers = new ArrayList<Voucher>();
		ResponseMassageWithCodeDto responseMassageWithCodeDto = new ResponseMassageWithCodeDto();

//		For temporary we can delete only one voucher
		for (VoucherDto voucherDto : listVoucherDtos) {
			voucher = voucherRepository.findById(Integer.parseInt(voucherDto.getVoucherID())).orElse(null);
			voucher.setDeleteStatus(voucherDto.getDeleteStatus());
			listOfVouchers.add(voucher);
		}

//		save all changes into the database 
		voucherRepository.saveAll(listOfVouchers);

		responseMassageWithCodeDto.setMassage("Delete status update successfully!!");
		responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
		return responseMassageWithCodeDto;
	}

	@Override
	public Map<String, Object> deleteIndividualVoucher(VoucherDto voucherDto) {
		// TODO Auto-generated method stub

		Map<String, Object> map = new LinkedHashMap<String, Object>();

		@SuppressWarnings("unchecked")
		VoucherMapper<VoucherDto, VoucherEntity> voucherMapper = (VoucherMapper<VoucherDto, VoucherEntity>) applicationContext
				.getBean(resolveVoucherMapper(voucherDto.getBillType()));

		if (voucherDto.getBillType().equalsIgnoreCase(XpendeskConstants.HOTEL_TYPE)) {

			List<VoucherDetailDto> listOfVoucherDetailDtos = new ArrayList<VoucherDetailDto>();
			HotelVoucher hotelVoucher = (HotelVoucher) voucherMapper.dtoToModel(voucherDto);
			map.put("status", "Delete Successfully");
			map.put("voucher_id", hotelVoucher.getVoucherID().getVoucherID().toString());
			map.put("hotel_voucher_id", hotelVoucher.getHotelVoucherID().toString());
			hotelVoucher.getListOfHotelVoucherDetail().forEach(hotelVoucherDetail -> {
				listOfVoucherDetailDtos.add(
						consolidatedHotelServiceBreakageMapper.convertHotelVoucherDetailModelToDto(hotelVoucherDetail));
			});
			hotelVoucher.getListOfFoodVoucherDetail().forEach(foodVoucherDetail -> {
				listOfVoucherDetailDtos.add(
						consolidatedHotelServiceBreakageMapper.convertFoodVoucherDetailModelToDto(foodVoucherDetail));
			});

			map.put("consolidated_hotel_service_breakage", listOfVoucherDetailDtos);

			return map;

		} else if (voucherDto.getBillType().equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)) {

			Voucher voucher = (Voucher) voucherMapper.dtoToModel(voucherDto);
			map.put("status", "Delete Successfully");
			map.put("food_voucher_id", voucher.getFoodVoucherID().getFoodVoucherID().toString());
			map.put("voucher_id", voucher.getVoucherID().toString());

			return map;

		} else if (voucherDto.getBillType().equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)) {

			Voucher voucher = (Voucher) voucherMapper.dtoToModel(voucherDto);
			map.put("status", "Submit Successfully");
			map.put("voucher_id", voucher.getVoucherID().toString());
			map.put("conveyance_voucher_id", voucher.getConveyanceVoucherID().getConveyanceVoucherID().toString());

			return map;

		}

		return map;
	}
}
