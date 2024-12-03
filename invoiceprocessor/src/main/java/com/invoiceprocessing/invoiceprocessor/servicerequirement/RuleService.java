package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.response.ConsolidatedBreakageSummaryDto;

public interface RuleService {
	
	public List<ConsolidatedBreakageSummaryDto> eligibilityCriteriaService(List<ConsolidatedBreakageSummaryDto> consolidatedBreakageSummaryDto);

}
