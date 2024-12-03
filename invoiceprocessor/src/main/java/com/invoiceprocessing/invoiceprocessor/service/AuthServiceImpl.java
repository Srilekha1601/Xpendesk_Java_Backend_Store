package com.invoiceprocessing.invoiceprocessor.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.model.City;
import com.invoiceprocessing.invoiceprocessor.model.Employee;
import com.invoiceprocessing.invoiceprocessor.model.EmployeeRole;
import com.invoiceprocessing.invoiceprocessor.model.Grade;
import com.invoiceprocessing.invoiceprocessor.model.Office;
import com.invoiceprocessing.invoiceprocessor.model.Role;
import com.invoiceprocessing.invoiceprocessor.repository.CityRepository;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRoleRepository;
import com.invoiceprocessing.invoiceprocessor.repository.GradeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.OfficeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.RoleRepository;
import com.invoiceprocessing.invoiceprocessor.response.EmployeeDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	CityRepository cityRepository;

	@Autowired
	GradeRepository gradeRepository;

	@Autowired
	OfficeRepository officeRepository;

	@Autowired
	EmployeeRoleRepository employeeRoleRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public Map<String, Object> saveEmployee(EmployeeDto employeeDto) {
		// TODO Auto-generated method stub

//		For response purpose
		Map<String, Object> responseMapper = new HashMap<String, Object>();

		Employee employee = new Employee();
		EmployeeRole employeeRole = new EmployeeRole();
		Role role = new Role();
		insertDataFromModel(employeeDto, employee, employeeRole, role);
		employee = employeeRepository.save(employee);
		role = roleRepository.save(role);
		employeeRole = employeeRoleRepository.save(employeeRole);

		responseMapper.put("massage", "Successfully Registered !!!");

		return responseMapper;
	}

	private void insertDataFromModel(EmployeeDto employeeDto, Employee employee, EmployeeRole employeeRole, Role role) {
		// TODO Auto-generated method stub

//		Generate the common employee information..(This is temporary)
		City city = cityRepository.findById(1).get();
		Grade grade = gradeRepository.findById(1).get();
		Office office = officeRepository.findById(1).get();

		employee.setCityID(city);
		employee.setOfficeID(office);
		employee.setGradeID(grade);

//		Now impotent informations of Employee

		employee.setName(employeeDto.getEmployeeName());
		employee.setUserName(employeeDto.getUserName());
		employee.setPinCode(Integer.parseInt(employeeDto.getPinCode()));
		employee.setEmailID(employeeDto.getEmail());
		employee.setManager(employeeRepository.findById(Integer.parseInt(employeeDto.getManagerId())).get());
		employee.setEmployeeCode(employeeDto.getEmployeeCode());
		employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));

//		set user Roles .....

		if (roleRepository.findAll().stream().anyMatch(
				anyRoleElement -> anyRoleElement.getRoleDescription().equalsIgnoreCase(employeeDto.getRole()))) {
			Optional<Role> roleElement = roleRepository.findAll().stream()
					.filter(roleEntity -> roleEntity.getRoleDescription().equalsIgnoreCase(employeeDto.getRole()))
					.findFirst();
			role = roleElement.get();
		} else {
			role.setRoleDescription("ROLE_".concat(employeeDto.getRole().toUpperCase()));
		}

//		Employee Role Cross Reference...

		employeeRole.setEmpliyeeID(employee);
		employeeRole.setRoleID(role);
	}

}
