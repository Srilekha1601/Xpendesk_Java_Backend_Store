package com.invoiceprocessing.invoiceprocessor.GlobalExceptionHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomeResponceEntityHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		String errorMessage = e.getMessage();
		
		// Customize your response here
		return ResponseEntity
                .status(400)
                .body("{\"message\": \"" + errorMessage + "\"}");
	}
}
