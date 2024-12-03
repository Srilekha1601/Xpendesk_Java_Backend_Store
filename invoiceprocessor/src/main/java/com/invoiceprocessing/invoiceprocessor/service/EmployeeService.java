package com.invoiceprocessing.invoiceprocessor.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.model.Employee;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRepository;
import com.invoiceprocessing.invoiceprocessor.response.EmployeeDto;
import com.invoiceprocessing.invoiceprocessor.response.ResponseChekPartnerRoleDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.UserService;

import io.micrometer.common.util.StringUtils;

@Service
public class EmployeeService implements UserService {

	@Autowired
	EmployeeRepository employeeRepository;

	@Override
	public EmployeeDto getAllEmployeeInfo(Integer employeeID) {
		Employee employee = employeeRepository.findById(employeeID).get();
		EmployeeDto employeeDto = new EmployeeDto();
		employeeDto.setEmployeeCode(employee.getEmployeeCode());
		employeeDto.setEmployeeName(employee.getName());
		employeeDto.setUserName(employee.getUserName());
		employeeDto.setPassword(employee.getPassword());
		employeeDto.setCityID(employee.getCityID().getCityID().toString());
		employeeDto.setPinCode(employee.getPinCode().toString());
		employeeDto.setOfficeID(employee.getOfficeID().getOfficeID().toString());
		employeeDto.setGradeID(employee.getGradeID().getGenerateID().toString());
		return employeeDto;
	}

	@Override
	public List<EmployeeDto> getEmployeeList(EmployeeDto employeeDto) {
		// TODO Auto-generated method stub
		List<Employee> listOfEmployee = employeeRepository
				.findByAssistantId(employeeRepository.findById(employeeDto.getEmployeeId()).get());
		return listEntityToListDto(listOfEmployee);

	}

	private List<EmployeeDto> listEntityToListDto(List<Employee> listOfEmployee) {
		// TODO Auto-generated method stub

		EmployeeDto employeeDto = null;
		List<EmployeeDto> listEmployeeDtos = new ArrayList<EmployeeDto>();

		for (Employee employee : listOfEmployee) {
			employeeDto = new EmployeeDto();
			employeeDto.setEmployeeId(employee.getEmployeeId());
			employeeDto.setEmployeeName(employee.getName());
			employeeDto.setRole(
					employee.getEmployeeRoleList().stream().findFirst().get().getRoleID().getRoleDescription());
			employeeDto.setGradeID(employee.getGradeID().getGrade());
			listEmployeeDtos.add(employeeDto);
		}

		return listEmployeeDtos;

	}

	@Override
	public List<EmployeeDto> getManagerEmployees(EmployeeDto employeeDto) {
		// TODO Auto-generated method stub

		List<EmployeeDto> listEmployeeDtos = new ArrayList<EmployeeDto>();

		Employee employee = employeeRepository.findById(employeeDto.getEmployeeId()).get().getManager();

		EmployeeDto employeeMapperDto = new EmployeeDto();

		employeeMapperDto.setEmployeeId(employee.getEmployeeId());

		employeeMapperDto.setEmployeeName(employee.getName());

		employeeMapperDto.setEmployeeCode(employee.getEmployeeCode());

		employeeMapperDto.setUserName(employee.getUserName());

		employeeMapperDto.setPinCode(employee.getPinCode().toString());

		employeeMapperDto.setEmail(employee.getEmailID());

		employeeMapperDto.setManagerId(employee.getManager().getEmployeeId().toString());

		listEmployeeDtos.add(employeeMapperDto);

		return listEmployeeDtos;
	}

	@Override
	public List<EmployeeDto> getAssistantInfoThroughManagerId(EmployeeDto employeeDto) {
		// TODO Auto-generated method stub

		Employee employee = employeeRepository.findById(employeeDto.getEmployeeId()).orElse(null);

		List<Employee> listOfAssistantEmployees = employeeRepository.findAllByManager(employee);

		EmployeeDto employeeMapperObj = null;

		List<EmployeeDto> listOfEmployeeDto = new ArrayList<EmployeeDto>();

		for (Employee employeeEntity : listOfAssistantEmployees) {
			employeeMapperObj = new EmployeeDto();
			employeeMapperObj.setEmployeeName(employeeEntity.getName());
			employeeMapperObj.setEmployeeId(employeeEntity.getEmployeeId());
			employeeMapperObj.setEmployeeCode(employeeEntity.getEmployeeCode());
			employeeMapperObj.setUserName(employeeEntity.getUserName());
			employeeMapperObj.setPinCode(employeeEntity.getPinCode().toString());
			employeeMapperObj.setEmail(employeeEntity.getEmailID());
			employeeMapperObj.setManagerId(employeeEntity.getManager().getEmployeeId().toString());

			listOfEmployeeDto.add(employeeMapperObj);

		}

		return listOfEmployeeDto;
	}

	@Override
	public ResponseChekPartnerRoleDto checkHasPartnerId(Integer employeeId) {

		ResponseChekPartnerRoleDto response = new ResponseChekPartnerRoleDto();

		Employee employee = employeeRepository.findById(employeeId).orElse(null);

		if (employee.getPartner() != null && !StringUtils.isBlank(employee.getPartner().getEmployeeId().toString())
				&& employee.getEmployeeId().toString()
						.equalsIgnoreCase(employee.getPartner().getEmployeeId().toString())) {
			response.setIsPartner(Boolean.TRUE);
			response.setStatus(HttpStatus.SC_OK);
			response.setMessage("Success!!");

		} else {
			response.setMessage("Has No Partner Role");
			response.setIsPartner(Boolean.FALSE);
			response.setStatus(HttpStatus.SC_OK);
		}

		return response;
	}

}
