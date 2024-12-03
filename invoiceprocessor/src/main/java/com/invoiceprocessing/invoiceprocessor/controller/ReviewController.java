package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.ConsolidatedBreakageSummaryDto;
import com.invoiceprocessing.invoiceprocessor.response.PartApprovalDto;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;
import com.invoiceprocessing.invoiceprocessor.response.ReviewInvoicesDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;
import com.invoiceprocessing.invoiceprocessor.response.WorkflowTaskDto;
import com.invoiceprocessing.invoiceprocessor.service.ReviewServiceImpl;

@RestController
@RequestMapping("/review")
public class ReviewController {

	@Autowired
	ReviewServiceImpl reviewServiceImpl;

	@PostMapping("/review-for-trip-voucher")
	public ResponseEntity<List<ConsolidatedBreakageSummaryDto>> reviewInvoices(
			@RequestBody ReviewInvoicesDto reviewInvoicesDto) {
		return new ResponseEntity<List<ConsolidatedBreakageSummaryDto>>(
				reviewServiceImpl.getListOfConsolidatedSummary(reviewInvoicesDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

	@PostMapping("/review-for-non-trip-voucher")
	public ResponseEntity<List<VoucherWithOutTripDto>> reviewInvoicesForNonTrip(
			@RequestBody ReviewInvoicesDto reviewInvoicesDto) {
		return new ResponseEntity<List<VoucherWithOutTripDto>>(
				reviewServiceImpl.getNonTripVoucherSummary(reviewInvoicesDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

	@PostMapping("/review-with-part-approval")
	public ResponseEntity<List<ConsolidatedBreakageSummaryDto>> partApprovalReview(
			@RequestBody List<ConsolidatedBreakageSummaryDto> listOfConsolidationBreakageSummaryDto) {
		return new ResponseEntity<List<ConsolidatedBreakageSummaryDto>>(
				reviewServiceImpl.saveConsolidatedSummary(listOfConsolidationBreakageSummaryDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

	@PostMapping("/partially-approval")
	public ResponseEntity<ResponseMassageWithCodeDto> partiallyApproval(
			@RequestBody List<PartApprovalDto> listOfPartApprovalDtoList) {
		return new ResponseEntity<ResponseMassageWithCodeDto>(
				reviewServiceImpl.partiallyApproval(listOfPartApprovalDtoList),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

	@PostMapping("/trip-confirmation")
	public ResponseEntity<ResponseMassageWithCodeDto> tripConfirmation(@RequestBody WorkflowTaskDto workflowTaskDto) {
		return new ResponseEntity<ResponseMassageWithCodeDto>(reviewServiceImpl.tripConfirmation(workflowTaskDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

}
