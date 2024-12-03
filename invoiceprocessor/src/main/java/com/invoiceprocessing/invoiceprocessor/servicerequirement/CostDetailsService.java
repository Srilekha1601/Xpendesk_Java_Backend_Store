package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.response.CostCodeDto;

public interface CostDetailsService {

	List<CostCodeDto> getDetailsForCost();

}
