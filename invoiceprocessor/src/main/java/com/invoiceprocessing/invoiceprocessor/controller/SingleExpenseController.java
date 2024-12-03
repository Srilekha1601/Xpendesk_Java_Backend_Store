package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.SingleVoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;
import com.invoiceprocessing.invoiceprocessor.service.SingleVoucherServiceImpl;

@RestController
@RequestMapping("/singleexpense")
public class SingleExpenseController {

	@Autowired
	SingleVoucherServiceImpl singleVoucherServiceImpl;

	@PostMapping("/expense")
	public ResponseEntity<List<SingleVoucherDto>> saveExpense(
			@RequestBody List<VoucherWithOutTripDto> listOfVoucherDto) {
		return ResponseEntity.ok(singleVoucherServiceImpl.singleVoucherSave(listOfVoucherDto));
	}

	@GetMapping("/reference/{referenceId}")
	public ResponseEntity<List<VoucherDto>> getSingleExpense(@PathVariable Integer referenceId) {
		return new ResponseEntity<List<VoucherDto>>(
				singleVoucherServiceImpl.getVoucherInfoWithRespectToEmployeeId(referenceId),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}
}
