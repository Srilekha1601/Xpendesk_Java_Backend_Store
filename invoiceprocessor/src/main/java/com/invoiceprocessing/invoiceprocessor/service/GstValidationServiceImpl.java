package com.invoiceprocessing.invoiceprocessor.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.repository.EntityRepository;
import com.invoiceprocessing.invoiceprocessor.response.GstEntityVerifiedDto;
import com.invoiceprocessing.invoiceprocessor.response.GstValidationDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.GstValidationService;

@Service
public class GstValidationServiceImpl implements GstValidationService {

	@Autowired
	EntityRepository entityRepository;

	@Value("${xpendesk.db.url}")
	private String url;

	@Value("${xpendesk.db.user}")
	private String user;

	@Value("${xpendesk.db.password}")
	private String password;

	@Override
	public GstEntityVerifiedDto checkValidation(GstValidationDto gstValidationDto) {
		// TODO Auto-generated method stub

		String query = "SELECT GSTIN FROM entity where entity_id=(select ENTITY_ID from employee where employee_id=?);";

		GstEntityVerifiedDto gstEntityVerifiedDto = null;

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, gstValidationDto.getEmployeeId());
			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				while (resultSet.next()) {
					gstEntityVerifiedDto = new GstEntityVerifiedDto();
					if (resultSet.getString("GSTIN").equalsIgnoreCase(gstValidationDto.getGstExtracted())) {
						gstEntityVerifiedDto.setGstEntityVerified("Y");
					} else {
						gstEntityVerifiedDto.setGstEntityVerified("N");
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return gstEntityVerifiedDto;
	}

}
