package com.invoiceprocessing.invoiceprocessor.mapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.invoiceprocessing.invoiceprocessor.model.Employee;
import com.invoiceprocessing.invoiceprocessor.model.ManagerAssistantBillAccessMapper;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRepository;
import com.invoiceprocessing.invoiceprocessor.response.AssistantDto;

@Mapper(componentModel = "spring")
public abstract class AssistantMapperUnderEmployee {

	@Autowired
	EmployeeRepository employeeRepository;

	@Mapping(target = "assistantAccessExpiryDate", source = "assistantDto.assistantAccessExpiryDate", qualifiedByName = "getCurrentDateConvertStringToDate")
	@Mapping(target = "manager", source = "assistantDto", qualifiedByName = "getManagerWithTheHelpOfManagerId")
	@Mapping(target = "mapperId", source = "assistantDto", qualifiedByName = "getMapperIdIfItIsPresent")
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdOn", ignore = true)
	@Mapping(target = "isDeleted", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "updatedOn", ignore = true)
	public abstract ManagerAssistantBillAccessMapper assiatantDtoToAssistantModel(AssistantDto assistantDto);

	@Mapping(target = "managerId", source = "managerAssistantBillAccessMapper", qualifiedByName = "setManagerById")
	@Mapping(target = "accessPermissionTimePeriod", ignore = true)
	@Mapping(target = "listOfAssignUserInfoDtos", ignore = true)
	public abstract AssistantDto assistantModelToAssistantDto(
			ManagerAssistantBillAccessMapper managerAssistantBillAccessMapper);

	@Named("setManagerById")
	protected String setManagerById(ManagerAssistantBillAccessMapper managerAssistantBillAccessMapper) {
		return managerAssistantBillAccessMapper.getManager().getEmployeeId().toString();
	}

	@Named("getCurrentDateConvertStringToDate")
	public Timestamp getCurrentDateConvertStringToDate(String currentDate) {
		try {
			if (currentDate != null && !currentDate.isEmpty()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

				LocalDateTime dateTime = LocalDateTime.parse(currentDate, formatter);

				return Timestamp.valueOf(dateTime);
			}
		} catch (DateTimeParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Named("getManagerWithTheHelpOfManagerId")
	public Employee getManagerWithTheHelpOfManagerId(AssistantDto assistantDto) {
		return !assistantDto.getManagerId().isEmpty()
				? employeeRepository.findById(Integer.parseInt(assistantDto.getManagerId())).get()
				: null;
	}

	@Named("getMapperIdIfItIsPresent")
	public Integer getMapperIdIfItIsPresent(AssistantDto assistantDto) {
		return assistantDto.getMapperId() == null ? null : Integer.parseInt(assistantDto.getMapperId());
	}

}
