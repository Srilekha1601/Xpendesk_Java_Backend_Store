package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.response.RejectRequestDto;

public interface RejectionRequestService {

	public List<RejectRequestDto> rejectRequest(RejectRequestDto rejectRequestDto);

}
