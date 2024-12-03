package com.invoiceprocessing.invoiceprocessor.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.model.Employee;
import com.invoiceprocessing.invoiceprocessor.model.PjpDetail;
import com.invoiceprocessing.invoiceprocessor.model.PjpHeader;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.PjpDetailRepository;
import com.invoiceprocessing.invoiceprocessor.repository.PjpHeaderRepository;
import com.invoiceprocessing.invoiceprocessor.response.PjpDto;
import com.invoiceprocessing.invoiceprocessor.response.PjpEmployeeDto;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.PjpService;

@Service
public class PjpServiceImpl implements PjpService {

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	PjpHeaderRepository pjpHeaderRepository;

	@Autowired
	PjpDetailRepository pjpDetailRepository;

	@Override
	public ResponseMassageWithCodeDto savePjp(PjpEmployeeDto pjpEmployeeDto) {
		// TODO Auto-generated method stub

//		Declaration section 
		PjpHeader pjpHeader = new PjpHeader();
		PjpDetail pjpDetail = null;
		ResponseMassageWithCodeDto responseMassageWithCodeDto = new ResponseMassageWithCodeDto();
		List<PjpDetail> listOfPjpDetails = new ArrayList<PjpDetail>();
		Employee employee = employeeRepository.findById(pjpEmployeeDto.getEmployeeId()).orElse(null);

//		for header section....

		pjpHeader.setEmployeeId(employee);
		pjpHeader.setMonth(pjpEmployeeDto.getMonth());
		pjpHeader.setYear(pjpEmployeeDto.getYear());

		pjpHeader = pjpHeaderRepository.save(pjpHeader);

//		for detail section....

		for (PjpDto pjpDto : pjpEmployeeDto.getListOfPjpDto()) {
			pjpDetail = new PjpDetail();
			pjpDetail.setPjpDate(pjpDto.getDate());
			pjpDetail.setPjpDescription(pjpDto.getDescription());
			pjpDetail.setPjpId(pjpHeader);

			listOfPjpDetails.add(pjpDetail);

		}

		if (!listOfPjpDetails.isEmpty()) {
			pjpDetailRepository.saveAll(listOfPjpDetails);
			responseMassageWithCodeDto.setMassage("PJP is saved successfully");
			responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
		}

		return responseMassageWithCodeDto;

	}

	@Override
	public PjpEmployeeDto getPjp(PjpEmployeeDto pjpEmployeeDto) {
		// TODO Auto-generated method stub

		PjpEmployeeDto pjpEmployeeMapperDto = new PjpEmployeeDto();
		PjpDto pjpDto = null;
		List<PjpDto> listOfPjpDto = new ArrayList<PjpDto>();

		Employee employee = employeeRepository.findById(pjpEmployeeDto.getEmployeeId()).orElse(null);

		PjpHeader pjpHeader = pjpHeaderRepository
				.findByEmployeeIdAndMonthAndYear(employee, pjpEmployeeDto.getMonth(), pjpEmployeeDto.getYear())
				.orElse(null);

//		............... For Header Section ..............

		pjpEmployeeMapperDto.setEmployeeId(pjpHeader.getEmployeeId().getEmployeeId());
		pjpEmployeeMapperDto.setMonth(pjpHeader.getMonth());
		pjpEmployeeMapperDto.setYear(pjpHeader.getYear());

//		............... For Detail section ..............

		List<PjpDetail> listOfPjpDetail = pjpDetailRepository.findByPjpId(pjpHeader);

		for (PjpDetail detail : listOfPjpDetail) {
			pjpDto = new PjpDto();
			pjpDto.setDate(detail.getPjpDate());
			pjpDto.setDescription(detail.getPjpDescription());
			pjpDto.setId(detail.getPjpDetailId().toString());

			listOfPjpDto.add(pjpDto);

		}

		pjpEmployeeMapperDto.setListOfPjpDto(listOfPjpDto);

		return pjpEmployeeMapperDto;
	}

}
