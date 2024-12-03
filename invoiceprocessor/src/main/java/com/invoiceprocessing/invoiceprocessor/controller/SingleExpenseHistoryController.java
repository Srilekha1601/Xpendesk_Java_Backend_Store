package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.SingleVoucherDto;
import com.invoiceprocessing.invoiceprocessor.service.SingleExpenseHistoryServiceImpl;

@RestController
@RequestMapping("/history")
public class SingleExpenseHistoryController {

	@Autowired
	SingleExpenseHistoryServiceImpl singleExpenseHistoryServiceImpl;

	@GetMapping("/outoftrip/{employeeId}")
	public ResponseEntity<List<SingleVoucherDto>> singleExpenseTripHistory(@PathVariable Integer employeeId) {
		return ResponseEntity.ok(singleExpenseHistoryServiceImpl.singleExpenseHistroyService(employeeId));
	}
}
