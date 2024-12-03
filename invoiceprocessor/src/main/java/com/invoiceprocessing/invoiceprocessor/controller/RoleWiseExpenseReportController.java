package com.invoiceprocessing.invoiceprocessor.controller;

import java.sql.SQLException;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.RoleWiseReportDto;
import com.invoiceprocessing.invoiceprocessor.service.RoleWiseReportServiceImpl;

@RestController
@RequestMapping("/report")
public class RoleWiseExpenseReportController {

	@Autowired
	RoleWiseReportServiceImpl roleWiseReportServiceImpl;

	@GetMapping("/rolewisereport")
	public ResponseEntity<List<RoleWiseReportDto>> getRoleWiseReport() throws SQLException {
		return ResponseEntity.ok(roleWiseReportServiceImpl.generateReportForAllUsers());
	}

	@GetMapping("/reportemployeewise/{employeeId}")
	public ResponseEntity<List<RoleWiseReportDto>> reportEmployeeWise(@PathVariable Integer employeeId)
			throws SQLException {
		return new ResponseEntity<List<RoleWiseReportDto>>(roleWiseReportServiceImpl.getReportForUser(employeeId),
				HttpStatusCode.valueOf(HttpStatus.SC_ACCEPTED));
	}

	@GetMapping("/employeewisereport/{employeeId}")
	public ResponseEntity<List<RoleWiseReportDto>> generateReportEmployeeWise(@PathVariable Integer employeeId)
			throws SQLException {
		return new ResponseEntity<List<RoleWiseReportDto>>(roleWiseReportServiceImpl.generateReportForUsers(employeeId),
				HttpStatusCode.valueOf(HttpStatus.SC_ACCEPTED));
	}

}
