package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.AssistantDto;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;
import com.invoiceprocessing.invoiceprocessor.service.AssistantServiceImpl;

@RestController
@RequestMapping("/assistant")
public class AssistantInfoController {

	@Autowired
	AssistantServiceImpl assistantServiceImpl;

	@PostMapping("/get-assistant-details")
	public ResponseEntity<List<AssistantDto>> assistantDetailsUnderEmployee(@RequestBody AssistantDto assistantDto) {
		return new ResponseEntity<List<AssistantDto>>(assistantServiceImpl.getAssistantInfo(assistantDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

	@PostMapping("/granting-access-to-assistant")
	public ResponseEntity<ResponseMassageWithCodeDto> grantAccessToAssistant(@RequestBody AssistantDto assistenDto) {
		return new ResponseEntity<ResponseMassageWithCodeDto>(assistantServiceImpl.grantAccessForAssistant(assistenDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

	@PostMapping("/get-manager-details-for-assistant")
	public ResponseEntity<Map<String, Object>> managerDetailsForAssistant(@RequestBody AssistantDto assistenDto) {
		return new ResponseEntity<Map<String, Object>>(assistantServiceImpl.getManagerDetailsForAssistant(assistenDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

}