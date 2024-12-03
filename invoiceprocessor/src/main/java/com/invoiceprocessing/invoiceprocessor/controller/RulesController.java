package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.ConsolidatedBreakageSummaryDto;
import com.invoiceprocessing.invoiceprocessor.service.RuleServiceImpl;

@RestController
@RequestMapping("/rules")
public class RulesController {

	@Autowired
	RuleServiceImpl ruleService;

	@PostMapping("/executionofrules")
	public ResponseEntity<List<ConsolidatedBreakageSummaryDto>> ruleExecution(
			@RequestBody List<ConsolidatedBreakageSummaryDto> consolidatedBreakageSummaryDtos) {
		return ResponseEntity.ok(ruleService.eligibilityCriteriaService(consolidatedBreakageSummaryDtos));
	}
}
