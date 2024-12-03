package com.invoiceprocessing.invoiceprocessor.controller;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.PjpEmployeeDto;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;
import com.invoiceprocessing.invoiceprocessor.service.PjpServiceImpl;

@RestController
@RequestMapping("/pjp")
public class PjpController {

	@Autowired
	PjpServiceImpl pjpServiceImpl;

	@PostMapping("/pjp-save")
	public ResponseEntity<ResponseMassageWithCodeDto> saveAllPjp(@RequestBody PjpEmployeeDto pjpEmployeeDto) {
		return new ResponseEntity<ResponseMassageWithCodeDto>(pjpServiceImpl.savePjp(pjpEmployeeDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

	@PostMapping("/get-pjp")
	public ResponseEntity<PjpEmployeeDto> getAllPjpsIn(@RequestBody PjpEmployeeDto pjpEmployeeDto) {
		return new ResponseEntity<PjpEmployeeDto>(pjpServiceImpl.getPjp(pjpEmployeeDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

}
