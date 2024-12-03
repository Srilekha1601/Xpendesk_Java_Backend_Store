package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.response.SingleVoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;

public interface SingleExpenseService {

	List<SingleVoucherDto> singleVoucherSave(List<VoucherWithOutTripDto> voucherDto);

	List<VoucherDto> getVoucherInfoWithRespectToEmployeeId(Integer referenceId);

}
