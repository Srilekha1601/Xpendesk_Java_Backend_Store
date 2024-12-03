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

import com.invoiceprocessing.invoiceprocessor.response.EmployeeDto;
import com.invoiceprocessing.invoiceprocessor.response.ResponseChekPartnerRoleDto;
import com.invoiceprocessing.invoiceprocessor.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@GetMapping("/employeeinfo/{employeeID}")
	public ResponseEntity<EmployeeDto> employeeInformation(@PathVariable Integer employeeID) {
		return ResponseEntity.ok(employeeService.getAllEmployeeInfo(employeeID));
	}

	@PostMapping("/employee-details")
	public ResponseEntity<List<EmployeeDto>> getTheEmployeeDetailsByEmployeeId(@RequestBody EmployeeDto employeeDto) {
		return ResponseEntity.ok(employeeService.getEmployeeList(employeeDto));
	}

	@PostMapping("/get-manager-info")
	public ResponseEntity<List<EmployeeDto>> getEmployeeManagerList(@RequestBody EmployeeDto employeeDto) {
		return ResponseEntity.ok(employeeService.getManagerEmployees(employeeDto));
	}

	@PostMapping("/get-assistant-details")
	public ResponseEntity<List<EmployeeDto>> getAssistantDetails(@RequestBody EmployeeDto employeeDto) {
		return ResponseEntity.ok(employeeService.getAssistantInfoThroughManagerId(employeeDto));
	}

	@GetMapping("/is-partner/{employeeId}")
	public ResponseEntity<ResponseChekPartnerRoleDto> getIsHasPartner(@PathVariable("employeeId") Integer employeeId) {
		return new ResponseEntity<ResponseChekPartnerRoleDto>(employeeService.checkHasPartnerId(employeeId),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

}
