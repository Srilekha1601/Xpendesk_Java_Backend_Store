package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;
import java.util.Map;

import com.invoiceprocessing.invoiceprocessor.model.VoucherEntity;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;

public interface VoucherService {

	VoucherEntity saveHotelVoucher(VoucherDto voucherDto);

	VoucherEntity saveFoodVoucher(VoucherDto foodVoucherDto);

	VoucherEntity saveConveyanceVoucher(VoucherDto conveyanceVoucherDto);

	List<VoucherWithOutTripDto> getVoucherInfoByVoucherId(VoucherWithOutTripDto voucherWithOutTripDto);

	ResponseMassageWithCodeDto deleteVoucherWithVoucherId(List<VoucherDto> listVoucherDtos);

	Map<String, Object> deleteIndividualVoucher(VoucherDto listVoucherDtos);

}
