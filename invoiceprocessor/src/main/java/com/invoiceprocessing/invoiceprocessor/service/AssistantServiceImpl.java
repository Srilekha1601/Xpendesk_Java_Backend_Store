package com.invoiceprocessing.invoiceprocessor.service;

import java.sql.Timestamp;
//import java.time.LocalDate;
//import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.mapper.AssistantMapperUnderEmployeeImpl;
import com.invoiceprocessing.invoiceprocessor.mapper.EmployeeMapperImpl;
import com.invoiceprocessing.invoiceprocessor.model.Employee;
import com.invoiceprocessing.invoiceprocessor.model.ManagerAssistantBillAccessMapper;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.ManagerAssistantBillAccessMapperRepository;
import com.invoiceprocessing.invoiceprocessor.response.AssignUserInfoDto;
import com.invoiceprocessing.invoiceprocessor.response.AssistantDto;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.AssistantService;

@Service
public class AssistantServiceImpl implements AssistantService {

	@Autowired
	ManagerAssistantBillAccessMapperRepository managerAssistantBillAccessMapperRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	AssistantMapperUnderEmployeeImpl assistantMapperUnderEmployeeImpl;

	@Autowired
	EmployeeMapperImpl employeeMapperImpl;

	@Override
	public List<AssistantDto> getAssistantInfo(AssistantDto assistantDto) {
		List<AssistantDto> listOfAssistantDtos = new ArrayList<AssistantDto>();
		Integer id = 0;
		Optional<Employee> optionalEmployee = employeeRepository
				.findById(Integer.parseInt(assistantDto.getManagerId()));
		Employee employee = optionalEmployee.get();
		List<ManagerAssistantBillAccessMapper> managerAssistantBillAccessMapper = managerAssistantBillAccessMapperRepository
				.findByManager(employee);

		for (ManagerAssistantBillAccessMapper managerAssistantBillAccessMapperEntity : managerAssistantBillAccessMapper) {
			if (!managerAssistantBillAccessMapperEntity.getAssistantId().toString().equalsIgnoreCase(id.toString())) {
				Optional<ManagerAssistantBillAccessMapper> optionalMapper = managerAssistantBillAccessMapperRepository
						.findByAssistantIdOrderByCreatedOnDesc(managerAssistantBillAccessMapperEntity.getAssistantId())
						.stream().findFirst();
				if (optionalMapper.get().getAssistantAccessExpiryDate() != null && Timestamp
						.valueOf(LocalDateTime.now()).after(optionalMapper.get().getAssistantAccessExpiryDate()))
					updateStatusByFetchAssistentEmployee(optionalMapper.get());
				id = optionalMapper.get().getAssistantId();
				listOfAssistantDtos
						.add(assistantMapperUnderEmployeeImpl.assistantModelToAssistantDto(optionalMapper.get()));

			}

		}
		return listOfAssistantDtos;
	}

	private void updateStatusByFetchAssistentEmployee(
			ManagerAssistantBillAccessMapper managerAssistantBillAccessMapper) {
		managerAssistantBillAccessMapper.setAssistantAccess("E");
		managerAssistantBillAccessMapperRepository.save(managerAssistantBillAccessMapper);
	}

	@Override
	public ResponseMassageWithCodeDto grantAccessForAssistant(AssistantDto assistantDto) {
		ResponseMassageWithCodeDto responseMassageWithCodeDto = new ResponseMassageWithCodeDto();

		if (assistantDto.getAssistantAccess().equalsIgnoreCase("P")) {

			ManagerAssistantBillAccessMapper managerAssistantBillAccessMapper = managerAssistantBillAccessMapperRepository
					.findByAssistantIdOrderByCreatedOnDesc(Integer.parseInt(assistantDto.getAssistantId())).stream()
					.findFirst().get();

			managerAssistantBillAccessMapper.setAssistantAccess("P");
			managerAssistantBillAccessMapper.setAssistantAccessExpiryDate(null);

			managerAssistantBillAccessMapperRepository.save(managerAssistantBillAccessMapper);

			responseMassageWithCodeDto.setMassage("Updated..");
			responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
			return responseMassageWithCodeDto;

		}

		if (assistantDto.getAssistantAccess().equalsIgnoreCase("G")) {
			LocalDateTime currentDateTime = LocalDateTime.now();

			// Add -- minutes to the current LocalDateTime
			LocalDateTime expiryDateTime = currentDateTime
					.plusMinutes(Integer.parseInt(assistantDto.getAccessPermissionTimePeriod()));
			assistantDto.setAssistantAccessExpiryDate(
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(expiryDateTime));
		}

		ManagerAssistantBillAccessMapper managerAssistantBillAccessMapper = assistantMapperUnderEmployeeImpl
				.assiatantDtoToAssistantModel(assistantDto);
		managerAssistantBillAccessMapper = managerAssistantBillAccessMapperRepository
				.save(managerAssistantBillAccessMapper);
		responseMassageWithCodeDto.setMassage("Saved..");
		responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
		return responseMassageWithCodeDto;

	}

	@Override
	public Map<String, Object> getManagerDetailsForAssistant(AssistantDto assistantDto) {

		List<ManagerAssistantBillAccessMapper> listOfManagerAssistantBillAccessMapper = new ArrayList<ManagerAssistantBillAccessMapper>();
		List<Employee> listOfEmployee = new ArrayList<Employee>();
		List<AssignUserInfoDto> listOfAssignUserInfoDtos = new ArrayList<AssignUserInfoDto>();
		Map<String, Object> mapAssignUserInfo = new HashMap<String, Object>();

		Boolean isAssistant = employeeRepository.findAll().stream()
				.anyMatch(employee -> employee.getAssistantId() != null
						? employee.getAssistantId().toString().equalsIgnoreCase(assistantDto.getAssistantId())
						: false);

		if (isAssistant) {

			ManagerAssistantBillAccessMapper managerAssistantBillAccess = managerAssistantBillAccessMapperRepository
					.findByAssistantIdOrderByCreatedOnDesc(Integer.parseInt(assistantDto.getAssistantId())).stream()
					.findFirst().get();
			if (managerAssistantBillAccess.getAssistantAccess().equalsIgnoreCase("P")) {
				mapAssignUserInfo.put("data", listOfAssignUserInfoDtos);
				mapAssignUserInfo.put("status", Boolean.TRUE);
				return mapAssignUserInfo;
			}

			List<ManagerAssistantBillAccessMapper> listOfManagerAssistantBillAccessMappers = managerAssistantBillAccessMapperRepository
					.findByAssistantIdOrderByCreatedOnDesc(Integer.parseInt(assistantDto.getAssistantId()));

			for (ManagerAssistantBillAccessMapper managerAssistantBillAccessMapper : listOfManagerAssistantBillAccessMappers) {
				if (managerAssistantBillAccessMapper.getAssistantAccess().equalsIgnoreCase("G")
						&& Timestamp.valueOf(LocalDateTime.now())
								.before(managerAssistantBillAccessMapper.getAssistantAccessExpiryDate())) {

					listOfManagerAssistantBillAccessMapper = managerAssistantBillAccessMapperRepository
							.findByAssistantId(Integer.parseInt(assistantDto.getAssistantId())).stream()
							.filter(managerAssistantBillAccessMapperEntity -> managerAssistantBillAccessMapperEntity
									.getAssistantAccess().equalsIgnoreCase("G")
									&& Timestamp.valueOf(LocalDateTime.now()).before(
											managerAssistantBillAccessMapperEntity.getAssistantAccessExpiryDate()))
							.collect(Collectors.toList());

				} else if (managerAssistantBillAccessMapper.getAssistantAccess().equalsIgnoreCase("G")
						&& Timestamp.valueOf(LocalDateTime.now())
								.after(managerAssistantBillAccessMapper.getAssistantAccessExpiryDate())) {

					ManagerAssistantBillAccessMapper managerAssistantBillAccessMapperEntity = managerAssistantBillAccessMapperRepository
							.findById(managerAssistantBillAccessMapper.getMapperId()).get();
					managerAssistantBillAccessMapperEntity.setAssistantAccess("E");
					managerAssistantBillAccessMapperRepository.save(managerAssistantBillAccessMapperEntity);

				}
			}

			if (!listOfManagerAssistantBillAccessMapper.isEmpty()) {
				listOfManagerAssistantBillAccessMapper.forEach(managerMapperEntity -> {
					listOfEmployee.add(managerMapperEntity.getManager());
				});

			}

			if (!listOfEmployee.isEmpty()) {
				listOfAssignUserInfoDtos = employeeMapperImpl.convertEmployeeListToDtoList(listOfEmployee);
				mapAssignUserInfo.put("data", listOfAssignUserInfoDtos);
				mapAssignUserInfo.put("status", Boolean.TRUE);
			}

			if (listOfManagerAssistantBillAccessMapper.isEmpty()) {
				mapAssignUserInfo.put("data", listOfAssignUserInfoDtos);
				mapAssignUserInfo.put("status", Boolean.TRUE);
			}

		} else {

			mapAssignUserInfo.put("data", null);
			mapAssignUserInfo.put("status", Boolean.FALSE);

		}

		return mapAssignUserInfo;
	}

}