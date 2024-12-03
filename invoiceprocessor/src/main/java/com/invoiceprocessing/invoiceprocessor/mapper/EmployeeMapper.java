package com.invoiceprocessing.invoiceprocessor.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.invoiceprocessing.invoiceprocessor.model.Employee;
import com.invoiceprocessing.invoiceprocessor.response.AssignUserInfoDto;

@Mapper(componentModel = "spring")
public abstract class EmployeeMapper {

	public abstract List<AssignUserInfoDto> convertEmployeeListToDtoList(List<Employee> listOfEmployee);

	@Mapping(target = "employeeDesignation", ignore = true)
	@Mapping(target = "employeeGrade", ignore = true)
	@Mapping(target = "employeeName", source = "employee.name")
	public abstract AssignUserInfoDto employeeDtoToAssignUserInfoDto(Employee employee);

}
