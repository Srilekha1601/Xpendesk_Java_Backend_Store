package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import com.invoiceprocessing.invoiceprocessor.response.DocumentVerificationDto;

public interface HashVerificationService {

	public DocumentVerificationDto verifyHashValue(String hashText);
	
}
