package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.CostCodeDto;
import com.invoiceprocessing.invoiceprocessor.response.ProjectDto;
import com.invoiceprocessing.invoiceprocessor.service.CostDetailsServiceImpl;
import com.invoiceprocessing.invoiceprocessor.service.ProjectDetailsServiceImpl;

@RestController
@RequestMapping("/project-cost-info")
public class ProjectOrCostDetailsController {

	@Autowired
	ProjectDetailsServiceImpl projectDetailsServiceImpl;

	@Autowired
	CostDetailsServiceImpl costDetailsServiceImpl;

	@GetMapping("/project-info")
	public ResponseEntity<List<ProjectDto>> getProjectInfo() {
		return new ResponseEntity<List<ProjectDto>>(projectDetailsServiceImpl.getDetailsForProject(),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

	@GetMapping("/cost-info")
	public ResponseEntity<List<CostCodeDto>> getCostInfo() {
		return new ResponseEntity<List<CostCodeDto>>(costDetailsServiceImpl.getDetailsForCost(),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

}
