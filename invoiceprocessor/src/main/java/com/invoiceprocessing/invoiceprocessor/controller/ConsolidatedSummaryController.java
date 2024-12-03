package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.mapper.ConsolidatedVoucherMapperImpl;
//import com.invoiceprocessing.invoiceprocessor.model.ConsolidatedBreakageSummary;
import com.invoiceprocessing.invoiceprocessor.response.ConsolidatedBreakageSummaryDto;
import com.invoiceprocessing.invoiceprocessor.response.HistoryOfConsolidatedData;
import com.invoiceprocessing.invoiceprocessor.response.ReferenceDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.service.ConsolidatedVoucherServiceImpl;

@CrossOrigin(origins = "http://192.168.1.142:3000, http://192.168.1.143:3000, http://192.168.1.141:3000", allowCredentials = "true")
@RestController
@RequestMapping("/consolidation")
public class ConsolidatedSummaryController {

	private static final String CONTEXT_FOR_CONSOLIDATION = "consolidation";

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	ConsolidatedVoucherServiceImpl connsolidatedVoucherServiceImpl;

	@Autowired
	ConsolidatedVoucherMapperImpl consolidatedVoucherMapperImpl;

	@GetMapping("/tripsummary/{tripId}")
	public ResponseEntity<List<ConsolidatedBreakageSummaryDto>> getConsolidatedSummaryList(
			@PathVariable Integer tripId) {
		ConsolidatedVoucherMapperImpl cvMapper = (ConsolidatedVoucherMapperImpl) applicationContext
				.getBean(CONTEXT_FOR_CONSOLIDATION);

		return ResponseEntity.ok(cvMapper.modelToDto(tripId));
	}

	@PostMapping("/consolidatedsummary")
	public ResponseEntity<Map<String, String>> saveConsolidatedSummary(
			@RequestBody ConsolidatedBreakageSummaryDto[] comnsoliBreakageSummaryDto) {
		Map<String, String> massage = new LinkedHashMap<String, String>();
		ConsolidatedVoucherMapperImpl consolidatedVoucherMapperImpl = (ConsolidatedVoucherMapperImpl) applicationContext
				.getBean(CONTEXT_FOR_CONSOLIDATION);
		List<ConsolidatedBreakageSummaryDto> consolidatedData = new ArrayList<ConsolidatedBreakageSummaryDto>();
		for (ConsolidatedBreakageSummaryDto consolidation : comnsoliBreakageSummaryDto) {
			consolidatedData.add(consolidation);
		}
		consolidatedVoucherMapperImpl.saveConsolicdatedData(consolidatedData);
		massage.put("message", "Submit Successfully");
		return ResponseEntity.ok(massage);
	}

	@GetMapping("/getallconsolidatedata/s/{employeeId}")
	public ResponseEntity<List<HistoryOfConsolidatedData>> getConsolidatedata(@PathVariable Integer employeeId) {
		ConsolidatedVoucherMapperImpl consolidatedVoucherMapperImpl = (ConsolidatedVoucherMapperImpl) applicationContext
				.getBean(CONTEXT_FOR_CONSOLIDATION);
		return ResponseEntity.ok(consolidatedVoucherMapperImpl.getAllHistory(employeeId));
	}

	@GetMapping("/getallconsolidatedata/d/{employeeId}")
	public ResponseEntity<List<HistoryOfConsolidatedData>> getConsolidatedataAsDraft(@PathVariable Integer employeeId) {
		ConsolidatedVoucherMapperImpl consolidatedVoucherMapperImpl = (ConsolidatedVoucherMapperImpl) applicationContext
				.getBean(CONTEXT_FOR_CONSOLIDATION);
		return ResponseEntity.ok(consolidatedVoucherMapperImpl.getAllHistoryOfDraftTrip(employeeId));
	}

	@GetMapping("/getallconsolidatedata/t/{employeeId}")
	public ResponseEntity<List<HistoryOfConsolidatedData>> getConsolidatedataAsTrip(@PathVariable Integer employeeId) {
		ConsolidatedVoucherMapperImpl consolidatedVoucherMapperImpl = (ConsolidatedVoucherMapperImpl) applicationContext
				.getBean(CONTEXT_FOR_CONSOLIDATION);
		return ResponseEntity.ok(consolidatedVoucherMapperImpl.getAllHistoryOfUpcomingTrip(employeeId));
	}

	@PostMapping("/listofconsolidateddata")
	public ResponseEntity<List<ConsolidatedBreakageSummaryDto>> saveAllConsolidatedData(
			@RequestBody List<VoucherDto> listOfVoucherDtos) {
		return ResponseEntity.ok(connsolidatedVoucherServiceImpl.saveAllConsolidatedData(listOfVoucherDtos));
	}

	@PostMapping("/trip-wise-consolidation")
	public ResponseEntity<List<ConsolidatedBreakageSummaryDto>> saveConsolidation(
			@RequestBody ReferenceDto referenceDto) {
		return ResponseEntity.ok(consolidatedVoucherMapperImpl.modelToDto(referenceDto.getReferenceId()));
	}

}
