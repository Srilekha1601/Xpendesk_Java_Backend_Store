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

import com.invoiceprocessing.invoiceprocessor.response.CreditCardTransactionsDto;
import com.invoiceprocessing.invoiceprocessor.response.EmployeeDto;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;
import com.invoiceprocessing.invoiceprocessor.service.CreditCardServiceImpl;

@RestController
@RequestMapping("/credit-card-transactions")
public class CreditCardTransactionsController {

	@Autowired
	CreditCardServiceImpl creditCardServiceImpl;

	@PostMapping("/get-transactions")
	public ResponseEntity<List<CreditCardTransactionsDto>> getTransactions(@RequestBody EmployeeDto employeeDto) {
		return new ResponseEntity<List<CreditCardTransactionsDto>>(
				creditCardServiceImpl.getTransactions(employeeDto.getEmployeeId()),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

	@PostMapping("/send-data-for-credit-card")
	public ResponseEntity<ResponseMassageWithCodeDto> saveDataForCreditCard(
			@RequestBody List<VoucherWithOutTripDto> voucherWithOutTripDtos) {
		return ResponseEntity.ok(creditCardServiceImpl.saveVoucherForCCTransaction(voucherWithOutTripDtos));
	}

	@PostMapping("/get-confirm-transaction")
	public ResponseEntity<List<CreditCardTransactionsDto>> getConfirmTransaction(@RequestBody EmployeeDto employeeDto) {
		return ResponseEntity.ok(creditCardServiceImpl.confirmTransaction(employeeDto));
	}

	@PostMapping("/add-voucher-in-trip")
	public ResponseEntity<ResponseMassageWithCodeDto> submitForNonTripVoucher(@RequestBody VoucherDto voucherDto) {
		return new ResponseEntity<ResponseMassageWithCodeDto>(creditCardServiceImpl.addVoucherInTrip(voucherDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

	@PostMapping("/change-for-single-expense")
	public ResponseEntity<ResponseMassageWithCodeDto> changeStatusForSingleExpense(@RequestBody VoucherDto voucherDto) {
		return new ResponseEntity<ResponseMassageWithCodeDto>(
				creditCardServiceImpl.updateVoucherIfSingleExpense(voucherDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

}
