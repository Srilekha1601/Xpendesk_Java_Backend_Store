package com.invoiceprocessing.invoiceprocessor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.GstEntityVerifiedDto;
import com.invoiceprocessing.invoiceprocessor.response.GstValidationDto;
import com.invoiceprocessing.invoiceprocessor.service.GstValidationServiceImpl;

@RestController
@RequestMapping("/validation")
public class GstValidationController {

	@Autowired
	GstValidationServiceImpl gstValidationServiceImpl;

	@PostMapping("/validategst")
	public ResponseEntity<GstEntityVerifiedDto> doValidation(@RequestBody GstValidationDto gstValidationDto) {
		return ResponseEntity.ok(gstValidationServiceImpl.checkValidation(gstValidationDto));
	}

	@PostMapping("/")
	public void validateGst() {

	}

}
