package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.ApprovalDto;
import com.invoiceprocessing.invoiceprocessor.service.ApprovalServiceImpl;

@RestController
@RequestMapping("/approve")
public class ApprovalController {

	@Autowired
	ApprovalServiceImpl approvalServiceImpl;

	@PostMapping("/approval")
	public ResponseEntity<List<ApprovalDto>> approvedRequests(@RequestBody ApprovalDto approvalDto) {
		return ResponseEntity.ok(approvalServiceImpl.approvalRequests(approvalDto));
	}

}
