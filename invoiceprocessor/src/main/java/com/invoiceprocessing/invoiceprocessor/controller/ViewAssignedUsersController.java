package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.AssignUserInfoDto;
import com.invoiceprocessing.invoiceprocessor.service.ViewAssignedUsersInfoServiceImpl;

@RestController
@RequestMapping("/view")
public class ViewAssignedUsersController {

	@Autowired
	ViewAssignedUsersInfoServiceImpl viewAssignedUsersInfoServiceImpl;

	@PostMapping("/users")
	public ResponseEntity<List<AssignUserInfoDto>> viewAssignedUsers(@RequestBody AssignUserInfoDto assignUserInfoDto) {
		return ResponseEntity.ok(viewAssignedUsersInfoServiceImpl.viewAssignedUsersInfo(assignUserInfoDto));
	}

}
