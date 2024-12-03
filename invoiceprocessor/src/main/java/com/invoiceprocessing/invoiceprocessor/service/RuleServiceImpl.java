package com.invoiceprocessing.invoiceprocessor.service;

//import static org.assertj.core.api.Assertions.entry;

//import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.stream.Collectors;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.mapper.RuleCheckMapper;
import com.invoiceprocessing.invoiceprocessor.response.ConsolidatedBreakageSummaryDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.RuleService;

@Service
public class RuleServiceImpl implements RuleService {

	@Autowired
	ApplicationContext applicationContext;

//	@Autowired
//	KieContainer kieContainer;

//	@Override
//	public List<ConsolidatedBreakageSummaryDto> eligibilityCriteriaService(
//			List<ConsolidatedBreakageSummaryDto> consolidatedBreakageSummaryDtos) {
//
//		List<ConsolidatedBreakageSummaryDto> eligibleConsolidatedDtoList = new ArrayList<ConsolidatedBreakageSummaryDto>();
//
//		for (ConsolidatedBreakageSummaryDto consolidatBreakageSummaryDto : consolidatedBreakageSummaryDtos) {
//			RuleCheckMapper ruleCheckaMapper = (RuleCheckMapper) applicationContext
//					.getBean(consolidatBreakageSummaryDto.getCategory());
//			KieContainer kieContainer = ruleCheckaMapper.checkRules();
//			KieSession kieSession = kieContainer.newKieSession();
//			kieSession.insert(consolidatBreakageSummaryDto);
//			kieSession.fireAllRules();
//			kieSession.dispose();
//			eligibleConsolidatedDtoList.add(consolidatBreakageSummaryDto);
//		}
//		return eligibleConsolidatedDtoList;
//	}

	@Override
	public List<ConsolidatedBreakageSummaryDto> eligibilityCriteriaService(
			List<ConsolidatedBreakageSummaryDto> consolidatedBreakageSummaryDtos) {

//		List<ConsolidatedBreakageSummaryDto> eligibleConsolidatedDtoList = new ArrayList<>();
//		System.out.println("Consolidated Breakage Summary:");
//		for (ConsolidatedBreakageSummaryDto dto : consolidatedBreakageSummaryDtos) {
//		    System.out.println(dto.toString());
//		}


		// Cache the RuleCheckMapper instances
		Map<String, RuleCheckMapper> ruleCheckMapperCache = new HashMap<>();
		for (ConsolidatedBreakageSummaryDto dto : consolidatedBreakageSummaryDtos) {  
			String category = dto.getCategory();
			if (!ruleCheckMapperCache.containsKey(category)) {
				ruleCheckMapperCache.put(category, (RuleCheckMapper) applicationContext.getBean(category));
			}
		}

		// Process DTOs in a single KieSession for each category
		for (Map.Entry<String, RuleCheckMapper> entry : ruleCheckMapperCache.entrySet()) {
			RuleCheckMapper ruleCheckMapper = entry.getValue();
			KieContainer kieContainer = ruleCheckMapper.checkRules();
			KieSession kieSession = kieContainer.newKieSession();

			// Insert all relevant DTOs into the session
			consolidatedBreakageSummaryDtos.stream().filter(dto -> dto.getCategory().equals(entry.getKey()))
					.forEach(kieSession::insert);

			// Fire rules and process results
			kieSession.fireAllRules();
			kieSession.dispose();
		}

		// Assuming rules mark eligible DTOs in some way (e.g., a field in the DTO)
//		eligibleConsolidatedDtoList = consolidatedBreakageSummaryDtos.stream()
//				.filter(ConsolidatedBreakageSummaryDto::isEligible).collect(Collectors.toList());
//		System.out.println("Consolidated Breakage Summary after rules:");
//		for (ConsolidatedBreakageSummaryDto dto : consolidatedBreakageSummaryDtos) {
//		    System.out.println(dto.toString());
//		}
		//System.out.println(consolidatedBreakageSummaryDtos);
		return consolidatedBreakageSummaryDtos;
	}

}
