package com.invoiceprocessing.invoiceprocessor.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.response.AssignUserInfoDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.ViewAssignedUsersInfoService;

@Service
public class ViewAssignedUsersInfoServiceImpl implements ViewAssignedUsersInfoService {

	@Value("${xpendesk.db.url}")
	private String url;

	@Value("${xpendesk.db.user}")
	private String user;

	@Value("${xpendesk.db.password}")
	private String password;

	@Override
	public List<AssignUserInfoDto> viewAssignedUsersInfo(AssignUserInfoDto assignUserInfo) {
		// TODO Auto-generated method stub

		List<AssignUserInfoDto> listOfAssignUserInfoDtoList = new ArrayList<AssignUserInfoDto>();
		AssignUserInfoDto assignUserInfoDto = null;

		String query = "select e.EMPLOYEE_ID AS EMPLOYEE_ID, e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n"
				+ "e.EMPLOYEE_NAME AS EMPLOYEE_NAME, g.GRADE AS GRADE, g.DESIGNATION AS DESIGNATION\r\n"
				+ "FROM employee e\r\n" + "INNER JOIN grade g ON e.GRADE_ID = g.GRADE_ID\r\n"
				+ "where e.MANAGER_ID = ?";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, assignUserInfo.getEmployeeId());
			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				while (resultSet.next()) {
					assignUserInfoDto = new AssignUserInfoDto();
					assignUserInfoDto.setEmployeeId(resultSet.getInt("EMPLOYEE_ID"));
					assignUserInfoDto.setEmployeeCode(resultSet.getString("EMPLOYEE_CODE"));
					assignUserInfoDto.setEmployeeName(resultSet.getString("EMPLOYEE_NAME"));
					assignUserInfoDto.setEmployeeGrade(resultSet.getString("GRADE"));
					assignUserInfoDto.setEmployeeDesignation(resultSet.getString("DESIGNATION"));

					listOfAssignUserInfoDtoList.add(assignUserInfoDto);

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listOfAssignUserInfoDtoList;
	}

}
