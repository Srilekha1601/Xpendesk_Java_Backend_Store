package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.response.ConsolidatedBreakageSummaryDto;
import com.invoiceprocessing.invoiceprocessor.response.PartApprovalDto;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;
import com.invoiceprocessing.invoiceprocessor.response.ReviewForVouchersAttributesDto;
import com.invoiceprocessing.invoiceprocessor.response.ReviewInvoicesDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;
import com.invoiceprocessing.invoiceprocessor.response.WorkflowTaskDto;

public interface ReviewService {

	List<VoucherDto> reviewVouchers(ReviewForVouchersAttributesDto reviewForVouchersAttributesDto);

	List<ConsolidatedBreakageSummaryDto> getListOfConsolidatedSummary(ReviewInvoicesDto reviewInvoicesDto);

	List<VoucherWithOutTripDto> getNonTripVoucherSummary(ReviewInvoicesDto reviewInvoicesDto);

	List<ConsolidatedBreakageSummaryDto> saveConsolidatedSummary(
			List<ConsolidatedBreakageSummaryDto> listOfConsolidatedBreakageSummaryDtos);

	ResponseMassageWithCodeDto partiallyApproval(List<PartApprovalDto> voucherDto);

	ResponseMassageWithCodeDto tripConfirmation(WorkflowTaskDto workflowTaskDto);

}
