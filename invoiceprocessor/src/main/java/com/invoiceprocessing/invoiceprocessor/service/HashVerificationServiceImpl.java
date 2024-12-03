package com.invoiceprocessing.invoiceprocessor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.repository.VoucherRepository;
import com.invoiceprocessing.invoiceprocessor.response.DocumentVerificationDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.HashVerificationService;

@Service
public class HashVerificationServiceImpl implements HashVerificationService {

	@Autowired
	VoucherRepository voucherRepository;

	@Override
	public DocumentVerificationDto verifyHashValue(String hashText) {
		DocumentVerificationDto documentVerificationDto = new DocumentVerificationDto();
		Integer count = 0;
		count = voucherRepository.existByHashValue(hashText);
		if (count > 0) {
			documentVerificationDto.setIsSubmitted(Boolean.TRUE);
			return documentVerificationDto;
		} else {
			documentVerificationDto.setIsSubmitted(Boolean.FALSE);
			return documentVerificationDto;
		}
	}

}
