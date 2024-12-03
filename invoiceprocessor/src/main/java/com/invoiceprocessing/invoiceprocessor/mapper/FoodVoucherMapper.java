package com.invoiceprocessing.invoiceprocessor.mapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.invoiceprocessing.invoiceprocessor.model.FoodVoucher;
import com.invoiceprocessing.invoiceprocessor.model.FoodVoucherDetail;
import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.repository.FoodVoucherDetailRepository;
import com.invoiceprocessing.invoiceprocessor.repository.FoodVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherRepository;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDetailDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;

@Component("foodVoucherMapper")
public class FoodVoucherMapper implements VoucherMapper<VoucherDto, Voucher> {

	@Autowired
	VoucherRepository vouchrRepository;
	@Autowired
	FoodVoucherRepository foodVoucherRepository;
	@Autowired
	FoodVoucherDetailRepository foodVoucherDetailRepository;
	@Autowired
	TripRepository tripRepository;

	@Override
	public Voucher dtoToModel(VoucherDto foodDto) {
		FoodVoucher foodVoucher = new FoodVoucher();
		Voucher voucherDetailForFood = new Voucher();
		FoodVoucherDetail foodVoucherDetail = null;
		List<FoodVoucherDetail> listOfFoodVoucherDetails = new ArrayList<FoodVoucherDetail>();

		if (foodDto != null) {
//			if (StringUtils.isNoneBlank(foodDto.getVoucherID())) {
//				foodVoucher = vouchrRepository.findById(Integer.parseInt(foodDto.getVoucherID())).get().getFoodVoucherID();
//			}
			dataForInsertion(foodDto, voucherDetailForFood, foodVoucher, foodVoucherDetail, listOfFoodVoucherDetails);
			voucherDetailForFood = vouchrRepository.save(voucherDetailForFood);

			// if manualEntry != N then:
			foodVoucher = foodVoucherRepository.save(foodVoucher);
			voucherDetailForFood.setFoodVoucherID(foodVoucher);
			if (!listOfFoodVoucherDetails.isEmpty())
				foodVoucherDetailRepository.saveAll(listOfFoodVoucherDetails);

		} else {
			return null;
		}
		return voucherDetailForFood;
	}

	@Override
	public VoucherDto modelToDto(Voucher target) {
		// TODO Auto-generated method stub
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

	private Trip getTripByTripId(Integer tripId) {
		Optional<Trip> tripEntity = tripRepository.findById(tripId);
		return tripEntity.get();
	}

	private void dataForInsertion(VoucherDto foodDto, Voucher voucherDetailForFood, FoodVoucher foodVoucher,
			FoodVoucherDetail foodVoucherDetail, List<FoodVoucherDetail> listOfFoodVoucherDetails) {
		if (!foodDto.getVoucherID().isEmpty())
			voucherDetailForFood.setVoucherID(Integer.parseInt(foodDto.getVoucherID()));
		voucherDetailForFood.setFileName(foodDto.getFilename());
		voucherDetailForFood.setInvoiceType(foodDto.getBillType());
		voucherDetailForFood
				.setTotalAmount(new BigDecimal(foodDto.getTotalAmount().equalsIgnoreCase("Not Found") ? "0.0"
						: foodDto.getTotalAmount().replace(",", "")));
		voucherDetailForFood.setMerchantName(foodDto.getMerchantName());
		Long utilDate = new java.util.Date().getTime();
		voucherDetailForFood.setVoucherDate(
				foodDto.getDate().replace("/", " ").equalsIgnoreCase("Not Found") ? new java.sql.Date(utilDate)
						: parseStringToDate(foodDto.getDate()));
		voucherDetailForFood.setInvoiceDate(parseStringToDate(foodDto.getDate()));
		voucherDetailForFood.setPaymentMode(foodDto.getPaymentMode());
		voucherDetailForFood.setExceptionReason(foodDto.getExceptionReason());
//		voucherDetailForFood.setDeleteStatus(foodDto.getDeleteStatus());
		voucherDetailForFood
				.setDeleteStatus(foodDto.getDeleteStatus() == null || foodDto.getDeleteStatus().equalsIgnoreCase("")
						|| foodDto.getDeleteStatus().equalsIgnoreCase("N") ? "N" : foodDto.getDeleteStatus());
		voucherDetailForFood.setInvoiceNumber(foodDto.getInvoiceNo());
		if (foodDto.getIsSingleExpense().equalsIgnoreCase("Y"))
			voucherDetailForFood.setIsSingleExpense(foodDto.getIsSingleExpense());
		voucherDetailForFood.setIsSingleExpense(foodDto.getIsSingleExpense());
		if (!foodDto.getTripId().isEmpty())
			voucherDetailForFood.setTrip(getTripByTripId(Integer.parseInt(foodDto.getTripId())));
		voucherDetailForFood.setHashText(foodDto.getHashedText());
		voucherDetailForFood.setClaimedAmount(new BigDecimal(foodDto.getClaimedAmount() == null
				? foodDto.getTotalAmount().equalsIgnoreCase("Not Found") ? "0.0" : foodDto.getTotalAmount()
				: foodDto.getClaimedAmount()));
		voucherDetailForFood.setFileImage(foodDto.getImage());
		voucherDetailForFood.setManualEntry(foodDto.getManualEntry());
		if (!foodDto.getTripId().isEmpty())
			voucherDetailForFood.setEmployeeID(
					tripRepository.findById(Integer.parseInt(foodDto.getTripId())).get().getEmployeeID());
//		Voucher foodVoucherInfo = vouchrRepository.save(voucherDetailForFood);

//		foodVoucher.setVoucherID(foodVoucherInfo);
		if (!foodDto.getFoodVoucherID().isEmpty())
			foodVoucher.setFoodVoucherID(Integer.parseInt(foodDto.getFoodVoucherID()));
		foodVoucher.setVoucherID(voucherDetailForFood);
//		foodVoucher.setContainsLiquor(Boolean.parseBoolean(foodDto.getLiquorStatus()));
		foodVoucher.setContainsLiquor(foodDto.getLiquorStatus().equalsIgnoreCase("Liquor is Present") ? true : false);
		foodVoucher.setTotalAmount(new BigDecimal(foodDto.getTotalAmount().equalsIgnoreCase("Not Found") ? "0.0"
				: foodDto.getTotalAmount().replace(",", "")));
//		foodVoucher.setCgstAmount(voucherDetailForFood.getFoodVoucherID() == null ? "0.0"
//				: voucherDetailForFood.getFoodVoucherID().getCgstAmount());
//		foodVoucher.setSgstAmount(voucherDetailForFood.getFoodVoucherID() == null ? "0.0"
//				: voucherDetailForFood.getFoodVoucherID().getSgstAmount());
		foodVoucher.setObjectionRaise(foodDto.getObjectionRaise() != null ? foodDto.getObjectionRaise() : "");
		foodVoucher.setCgstAmount(
				foodDto.getCgstAmount().equalsIgnoreCase("Not Found") || foodDto.getCgstAmount().equalsIgnoreCase("N/A")
						|| foodDto.getCgstAmount().equalsIgnoreCase("") ? "0.0" : foodDto.getCgstAmount());
		foodVoucher.setSgstAmount(
				foodDto.getSgstAmount().equalsIgnoreCase("Not Found") || foodDto.getSgstAmount().equalsIgnoreCase("N/A")
						|| foodDto.getSgstAmount().equalsIgnoreCase("") ? "0.0" : foodDto.getSgstAmount());
		foodVoucher.setGstAmount(new BigDecimal(
				foodDto.getGstNo().equalsIgnoreCase("N/A") || foodDto.getGstNo().equalsIgnoreCase("") ? "0.0"
						: foodDto.getGstNo().replace(",", "")));
		foodVoucher.setClaimedAmount(new BigDecimal(
				foodDto.getClaimedAmount() == null
						? foodDto.getTotalAmount().equalsIgnoreCase("Not Found") ? "0.0"
								: foodDto.getTotalAmount().replace(",", "")
						: foodDto.getClaimedAmount()));
//		FoodVoucher foodVoucherData = foodVoucherRepository.save(foodVoucher);

		for (VoucherDetailDto foodVoucherDetails : foodDto.getConsolidatedHotelServiceBreakage()) {
			foodVoucherDetail = new FoodVoucherDetail();
			foodVoucherDetail.setFoodVoucherID(foodVoucher);
			foodVoucherDetail.setAmount(new BigDecimal(foodVoucherDetails.getAmount()));
			foodVoucherDetail.setBillType(foodVoucherDetails.getServiceType());
			foodVoucherDetail.setDescription(foodVoucherDetails.getDescription());
			foodVoucherDetail.setCgstAmount(new BigDecimal(foodVoucherDetails.getGst1()));
			foodVoucherDetail.setSgstAmount(new BigDecimal(foodVoucherDetails.getGst2()));
			foodVoucherDetail.setGstAmount(
					new BigDecimal(foodVoucherDetails.getGst1()).add(new BigDecimal(foodVoucherDetails.getGst2())));
			listOfFoodVoucherDetails.add(foodVoucherDetail);
		}

	}

}
