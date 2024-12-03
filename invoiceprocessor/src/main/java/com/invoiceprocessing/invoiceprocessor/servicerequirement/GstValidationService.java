package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import com.invoiceprocessing.invoiceprocessor.response.GstEntityVerifiedDto;
import com.invoiceprocessing.invoiceprocessor.response.GstValidationDto;

public interface GstValidationService {

	GstEntityVerifiedDto checkValidation(GstValidationDto gstValidationDto);

}
