package com.invoiceprocessing.invoiceprocessor.mapper;

import java.math.BigDecimal;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.invoiceprocessing.invoiceprocessor.model.FoodVoucher;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.repository.FoodVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherRepository;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;

@Mapper(componentModel = "spring")
public abstract class SingleVoucherMapperForFood {

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	FoodVoucherRepository foodVoucherRepository;

	@Mapping(target = "voucherID", source = "voucherWithOutTripDto", qualifiedByName = "convertingVouherIdToVoucher")
	@Mapping(target = "containsLiquor", source = "voucherWithOutTripDto.liquorStatus", qualifiedByName = "convertStringToBoolean")
	@Mapping(target = "foodVoucherID", source = "voucherWithOutTripDto.foodVoucherId", qualifiedByName = "ckeckIfFoodVoucherIsPresent")
	@Mapping(target = "gstAmount", source = "voucherWithOutTripDto", qualifiedByName = "getGstAmountCalculation")
	@Mapping(target = "listOfFoodVoucherDetails", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdOn", ignore = true)
	@Mapping(target = "isDeleted", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "updatedOn", ignore = true)
	public abstract FoodVoucher singleVoucherMapperToSingleExpense(VoucherWithOutTripDto voucherWithOutTripDto);

	@InheritInverseConfiguration
	@Mapping(target = "foodVoucherId", source = "foodVoucher.foodVoucherID", qualifiedByName = "foodVoucherToFoodVoucherId")
	@Mapping(target = "billType", source = "foodVoucher", qualifiedByName = "checkTheTypeOfTheInvoice")
	@Mapping(target = "date", source = "foodVoucher", qualifiedByName = "getDateFromVoucher")
	@Mapping(target = "filename", source = "foodVoucher", qualifiedByName = "getFileNameFromVoucher")
	@Mapping(target = "hashedText", source = "foodVoucher", qualifiedByName = "getHashTextFromVoucher")
	@Mapping(target = "image", source = "foodVoucher", qualifiedByName = "getImageFromVoucher")
	@Mapping(target = "voucherID", source = "foodVoucher", qualifiedByName = "convertVocuherToStringVoucherId")
	@Mapping(target = "liquorStatus", source = "foodVoucher", qualifiedByName = "convertBooleanToStringForLiquor")
	@Mapping(target = "invoiceNo", source = "foodVoucher", qualifiedByName = "getInvoiceNumberFromTheVoucher")
	@Mapping(target = "isSingleExpense", source = "foodVoucher", qualifiedByName = "getStatusFromVoucher")
	@Mapping(target = "manualEntry", source = "foodVoucher", qualifiedByName = "checkForManualEntry")
	@Mapping(target = "merchantName", source = "foodVoucher", qualifiedByName = "checkMerchantNameForFoodVoucher")
	@Mapping(target = "isOthersClassified", ignore = true)
	@Mapping(target = "fromLocation", ignore = true)
	@Mapping(target = "interIntraCity", ignore = true)
	@Mapping(target = "companyName", ignore = true)
	@Mapping(target = "gstNo", ignore = true)
	@Mapping(target = "gstFormat", ignore = true)
	@Mapping(target = "checkinDate", ignore = true)
	@Mapping(target = "consolidatedHotelServiceBreakage", ignore = true)
	@Mapping(target = "currency", ignore = true)
	@Mapping(target = "checkinDateFormat", ignore = true)
	@Mapping(target = "checkoutDate", ignore = true)
	@Mapping(target = "intraOrInterCityTravel", ignore = true)
	@Mapping(target = "guestName", ignore = true)
	@Mapping(target = "checkoutDateFormat", ignore = true)
	@Mapping(target = "arrivalDate", ignore = true)
	@Mapping(target = "arrivalTime", ignore = true)
	@Mapping(target = "departureDate", ignore = true)
	@Mapping(target = "departureTime", ignore = true)
	@Mapping(target = "description", ignore = true)
	@Mapping(target = "hotelCity", ignore = true)
	@Mapping(target = "hotelPin", ignore = true)
	@Mapping(target = "hotelState", ignore = true)
	@Mapping(target = "modeOfTravel", ignore = true)
	@Mapping(target = "hotelServiceBreakage", ignore = true)
	@Mapping(target = "employeeId", ignore = true)
	@Mapping(target = "noOfKm", ignore = true)
	@Mapping(target = "pax", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "toLocation", ignore = true)
	@Mapping(target = "totalAmountFormat", ignore = true)
	@Mapping(target = "totalDaysStayed", ignore = true)
	@Mapping(target = "travelClass", ignore = true)
	@Mapping(target = "dateFormat", ignore = true)
	@Mapping(target = "tripID", ignore = true)
	@Mapping(target = "travelTicketClass", ignore = true)
	public abstract VoucherWithOutTripDto singleVoucherToVoucherWithOutTripDto(FoodVoucher foodVoucher);

	@Named("convertingVouherIdToVoucher")
	protected Voucher convertingVouherIdToVoucher(VoucherWithOutTripDto voucherWithOutTripDto) {
		return voucherWithOutTripDto.getVoucherID().isEmpty() ? null
				: voucherRepository.findById(Integer.parseInt(voucherWithOutTripDto.getVoucherID())).get();
	}

	@Named("convertStringToBoolean")
	protected Boolean convertStringToBoolean(String liquorStatus) {
		if (liquorStatus.equalsIgnoreCase("Liquor is Present"))
			return true;
		else {
			return false;
		}
	}

	@Named("ckeckIfFoodVoucherIsPresent")
	protected Integer ckeckIfFoodVoucherIsPresent(String foodVoucherId) {
		return foodVoucherId.isEmpty() ? null
				: foodVoucherRepository.findById(Integer.parseInt(foodVoucherId)).get().getFoodVoucherID();
	}

	@Named("foodVoucherToFoodVoucherId")
	protected String ckeckIfFoodVoucherIsPresent(Integer foodVoucherId) {
		return foodVoucherId.toString();
	}

	@Named("checkTheTypeOfTheInvoice")
	protected String checkTheTypeOfTheInvoice(FoodVoucher foodVoucher) {
		return foodVoucher.getVoucherID() != null ? foodVoucher.getVoucherID().getInvoiceType() : null;
	}

	@Named("checkCgstAmount")
	protected String checkCgstAmount(Voucher voucher) {
		return voucher.getFoodVoucherID().getCgstAmount();
	}

	@Named("getGstAmountCalculation")
	protected BigDecimal getGstAmountCalculation(VoucherWithOutTripDto voucherWithOutTripDto) {
		return new BigDecimal(voucherWithOutTripDto.getCgstAmount())
				.add(new BigDecimal(voucherWithOutTripDto.getCgstAmount()));
	}

	@Named("getDateFromVoucher")
	protected String getDateFromVoucher(FoodVoucher foodVoucher) {
		return DateMapper.dateToString(foodVoucher.getVoucherID().getVoucherDate());
	}

	@Named("getFileNameFromVoucher")
	protected String getFileNameFromVoucher(FoodVoucher foodVoucher) {
		return foodVoucher.getVoucherID().getFileName();
	}

	@Named("getHashTextFromVoucher")
	protected String getHashTextFromVoucher(FoodVoucher foodVoucher) {
		return foodVoucher.getVoucherID().getHashText();
	}

	@Named("getImageFromVoucher")
	protected byte[] getImageFromVoucher(FoodVoucher foodVoucher) {
		return foodVoucher.getVoucherID().getFileImage();
	}

	@Named("convertVocuherToStringVoucherId")
	protected String convertVocuherToStringVoucherId(FoodVoucher foodVoucher) {
		return foodVoucher.getVoucherID() != null ? foodVoucher.getVoucherID().getVoucherID().toString() : null;
	}

	@Named("convertBooleanToStringForLiquor")
	protected String convertBooleanToStringForLiquor(FoodVoucher foodVoucher) {
		if (foodVoucher.getContainsLiquor())
			return "Liquor is Present";
		else
			return "Liquor is Not Present";
	}

	@Named("getInvoiceNumberFromTheVoucher")
	protected String getInvoiceNumberFromTheVoucher(FoodVoucher foodVoucher) {
		return foodVoucher.getVoucherID() != null ? foodVoucher.getVoucherID().getInvoiceNumber() : null;
	}

	@Named("getStatusFromVoucher")
	protected String getStatusFromVoucher(FoodVoucher foodVoucher) {
		return foodVoucher.getVoucherID() != null ? foodVoucher.getVoucherID().getIsSingleExpense() : null;
	}

	@Named("checkForManualEntry")
	protected String checkForManualEntry(FoodVoucher foodVoucher) {
		return foodVoucher.getVoucherID() != null ? foodVoucher.getVoucherID().getManualEntry() : null;
	}

	@Named("checkMerchantNameForFoodVoucher")
	protected String checkMerchantNameForFoodVoucher(FoodVoucher foodVoucher) {
		return foodVoucher.getVoucherID() != null ? foodVoucher.getVoucherID().getMerchantName() : null;
	}

}
