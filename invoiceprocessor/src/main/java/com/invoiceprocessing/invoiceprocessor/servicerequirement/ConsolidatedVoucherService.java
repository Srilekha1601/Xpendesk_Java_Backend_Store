package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.response.ConsolidatedBreakageSummaryDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;

public interface ConsolidatedVoucherService {

	List<ConsolidatedBreakageSummaryDto> saveAllConsolidatedData(List<VoucherDto> listOfVoucherDtos);

}
