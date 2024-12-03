package com.invoiceprocessing.invoiceprocessor.controller;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.DocumentVerificationDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.service.HashVerificationServiceImpl;

@RestController
@RequestMapping("/document")
public class DuplicateDocumentVerificationController {

	@Autowired
	HashVerificationServiceImpl hashVerificationServiceImpl;

	@PostMapping("/verify-document")
	public ResponseEntity<DocumentVerificationDto> doVerification(@RequestBody VoucherDto voucherDto) {
		return new ResponseEntity<DocumentVerificationDto>(
				hashVerificationServiceImpl.verifyHashValue((voucherDto.getHashedText())),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

}
