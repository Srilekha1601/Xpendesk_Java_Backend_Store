package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;
import java.util.Map;

import com.invoiceprocessing.invoiceprocessor.response.AssistantDto;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;

public interface AssistantService {

	List<AssistantDto> getAssistantInfo(AssistantDto assistantDto);

	ResponseMassageWithCodeDto grantAccessForAssistant(AssistantDto assistantDto);

	Map<String, Object> getManagerDetailsForAssistant(AssistantDto assistantDto);

}