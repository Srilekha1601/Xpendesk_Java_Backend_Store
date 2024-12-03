package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.Map;

import com.invoiceprocessing.invoiceprocessor.response.EmployeeDto;

public interface AuthService {

	public Map<String, Object> saveEmployee(EmployeeDto employeeDto);

}
