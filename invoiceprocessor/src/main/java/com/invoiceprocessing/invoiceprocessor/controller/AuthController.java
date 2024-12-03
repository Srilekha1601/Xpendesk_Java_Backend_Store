package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.AuthRequestBody;
import com.invoiceprocessing.invoiceprocessor.response.EmployeeDto;
import com.invoiceprocessing.invoiceprocessor.response.LoginResponseDto;
import com.invoiceprocessing.invoiceprocessor.securityconfiguration.XpendeskUserDetails;
import com.invoiceprocessing.invoiceprocessor.service.AuthServiceImpl;
import com.invoiceprocessing.invoiceprocessor.utils.JwtUtils;

@CrossOrigin(origins = "http://192.168.1.142:3000, http://192.168.1.143:3000, http://192.168.1.141:3000", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	AuthServiceImpl authServiceImpl;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> loginUserAndGetToken(@RequestBody AuthRequestBody authRequestBody) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequestBody.getUserName(), authRequestBody.getPassword()));
		if (authentication.isAuthenticated()) {
			XpendeskUserDetails userDetails = (XpendeskUserDetails) authentication.getPrincipal();
			LoginResponseDto loginResponseDto = new LoginResponseDto();
			loginResponseDto.setUserName(userDetails.getUsername());
			loginResponseDto.setEmployeeId(userDetails.getEmployeeId());
			loginResponseDto.setListOfRole(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList()));
			loginResponseDto.setEmployeeName(userDetails.getName());
			loginResponseDto.setToken(jwtUtils.generateToken(authentication.getName()));
			return ResponseEntity.ok(loginResponseDto);
		} else
			throw new UsernameNotFoundException("User Not Found !!");
	}

	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> registerUser(@RequestBody EmployeeDto authRequestBody) {
		return ResponseEntity.ok(authServiceImpl.saveEmployee(authRequestBody));

	}
}
