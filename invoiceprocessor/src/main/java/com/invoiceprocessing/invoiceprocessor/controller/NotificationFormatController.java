package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.NotificationDto;
import com.invoiceprocessing.invoiceprocessor.service.NotificationServiceImpl;

@RestController
@RequestMapping("/notification")
public class NotificationFormatController {

	@Autowired
	NotificationServiceImpl notificationServiceImpl;

	@PostMapping("/usernotification")
	public ResponseEntity<List<NotificationDto>> sendNotification(@RequestBody NotificationDto notification) {
		return ResponseEntity.ok(notificationServiceImpl.getNotification(notification));
	}

}
