package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.response.EmployeeDto;
import com.invoiceprocessing.invoiceprocessor.response.ResponseChekPartnerRoleDto;

public interface UserService {

	public EmployeeDto getAllEmployeeInfo(Integer employeeID);

	public List<EmployeeDto> getEmployeeList(EmployeeDto employeeDto);

	public List<EmployeeDto> getManagerEmployees(EmployeeDto employeeDto);

	public List<EmployeeDto> getAssistantInfoThroughManagerId(EmployeeDto employeeDto);

	public ResponseChekPartnerRoleDto checkHasPartnerId(Integer employeeId);

}
