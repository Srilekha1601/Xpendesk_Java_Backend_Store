package com.invoiceprocessing.invoiceprocessor.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.model.CostCode;
import com.invoiceprocessing.invoiceprocessor.repository.CostCodeRepository;
import com.invoiceprocessing.invoiceprocessor.response.CostCodeDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.CostDetailsService;

@Service
public class CostDetailsServiceImpl implements CostDetailsService {

	@Autowired
	CostCodeRepository costCodeRepository;

	@Override
	public List<CostCodeDto> getDetailsForCost() {

		List<CostCode> listOfCostCodeDtos = costCodeRepository.findAll();
		return costCodeListToCostCodeDtoList(listOfCostCodeDtos);

	}

	private List<CostCodeDto> costCodeListToCostCodeDtoList(List<CostCode> listOfCostCodeDtos) {

		CostCodeDto costCodeDto = new CostCodeDto();
		List<CostCodeDto> listCostCodeDtos = new ArrayList<CostCodeDto>();

		for (CostCode costCode : listOfCostCodeDtos) {

			costCodeDto = new CostCodeDto();
			costCodeDto.setCostCode(costCode.getCostCode());
			costCodeDto.setCostId(costCode.getCostId());
			costCodeDto.setIsEnable(costCode.getIsEnable());
			costCodeDto.setCostDescription(costCode.getCostDescription());

			listCostCodeDtos.add(costCodeDto);

		}

		return listCostCodeDtos;

	}

}
