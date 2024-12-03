package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.sql.SQLException;
import java.util.List;

import com.invoiceprocessing.invoiceprocessor.response.RoleWiseReportDto;

public interface RoleWiseReportService {

	List<RoleWiseReportDto> getReportForUser(Integer employeeId) throws SQLException;

	List<RoleWiseReportDto> generateReportForAllUsers() throws SQLException;

	List<RoleWiseReportDto> generateReportForUsers(Integer employeeId) throws SQLException;

}
