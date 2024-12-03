package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.RejectRequestDto;
import com.invoiceprocessing.invoiceprocessor.service.RejectRequestServiceImpl;

@RestController
@RequestMapping("/canclerequest")
public class RejectRequestController {

	@Autowired
	RejectRequestServiceImpl rejectRequestServiceImpl;

	@PostMapping("/cancle")
	public ResponseEntity<List<RejectRequestDto>> cancleRequest(@RequestBody RejectRequestDto rejectRequestDto) {
		return ResponseEntity.ok(rejectRequestServiceImpl.rejectRequest(rejectRequestDto));
	}

}
