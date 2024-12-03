package com.invoiceprocessing.invoiceprocessor.mapper;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import com.invoiceprocessing.invoiceprocessor.model.FoodVoucherDetail;
import com.invoiceprocessing.invoiceprocessor.model.HotelVoucherDetail;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDetailDto;

@Component
public class ConsolidatedHotelServiceBreakageMapper {

	public VoucherDetailDto convertHotelVoucherDetailModelToDto(HotelVoucherDetail hotelVoucherDetail) {
		VoucherDetailDto voucherDetailDto = new VoucherDetailDto();
		voucherDetailDto.setAmount(hotelVoucherDetail.getAmount().toString());
		voucherDetailDto.setServiceType(hotelVoucherDetail.getBillType());
		voucherDetailDto.setDescription(hotelVoucherDetail.getDescription());
		voucherDetailDto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(hotelVoucherDetail.getDate()));
		voucherDetailDto.setGst1(hotelVoucherDetail.getCgstAmount().toString());
		voucherDetailDto.setGst2(hotelVoucherDetail.getSgstAmount().toString());
		voucherDetailDto.setDetailVoucherID(hotelVoucherDetail.getHotelVoucherDetailId().toString());

		return voucherDetailDto;
	}

	public VoucherDetailDto convertFoodVoucherDetailModelToDto(FoodVoucherDetail foodVoucherDetail) {
		VoucherDetailDto voucherDetailDto = new VoucherDetailDto();
		voucherDetailDto.setAmount(foodVoucherDetail.getAmount().toString());
		voucherDetailDto.setServiceType(foodVoucherDetail.getBillType());
		voucherDetailDto.setDescription(foodVoucherDetail.getDescription());
		voucherDetailDto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(foodVoucherDetail.getItemDate()));
		voucherDetailDto.setGst1(foodVoucherDetail.getCgstAmount().toString());
		voucherDetailDto.setGst2(foodVoucherDetail.getSgstAmount().toString());
		voucherDetailDto.setDetailVoucherID(foodVoucherDetail.getFoodVoucherDetailID().toString());

		return voucherDetailDto;
	}

}
