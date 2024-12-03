package com.invoiceprocessing.invoiceprocessor.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.response.SingleVoucherDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.SingleExpenseHistoryService;

@Service
public class SingleExpenseHistoryServiceImpl implements SingleExpenseHistoryService {

	@Value("${xpendesk.db.url}")
	private String url;

	@Value("${xpendesk.db.user}")
	private String user;

	@Value("${xpendesk.db.password}")
	private String password;

	@Override
	public List<SingleVoucherDto> singleExpenseHistroyService(Integer employeeId) {
		// TODO Auto-generated method stub

		String query = "select single_voucher_detail_view.INVOICE_TYPE, \r\n"
				+ "single_voucher_detail_view.VOUCHER_ID, \r\n" + "single_voucher_detail_view.projects, \r\n"
				+ "single_voucher_detail_view.costs,\r\n" + "single_voucher_detail_view.VOUCHER_DATE, \r\n"
				+ "single_voucher_detail_view.TOTAL_AMOUNT, \r\n" + "wt.action_type as EXPENSE_STATUS\r\n" + "from\r\n"
				+ "(select v.INVOICE_TYPE, v.VOUCHER_ID as VOUCHER_ID, \r\n"
				+ "(select group_concat(p.project_code) from voucher_projects vp, \r\n"
				+ "project p where vp.PROJECT_ID=p.project_id and vp.VOUCHER_ID=v.VOUCHER_ID) as projects,\r\n"
				+ "(select group_concat(c.cost_code) from voucher_costs vc , cost_code c where vc.cost_id=c.cost_id and vc.VOUCHER_ID=v.VOUCHER_ID) as costs,\r\n"
				+ "v.VOUCHER_DATE as VOUCHER_DATE, v.TOTAL_AMOUNT  from voucher v where\r\n"
				+ "v.EMPLOYEE_ID = ? and v.SINGLE_EXPENSE = \"Y\"\r\n"
				+ "and (v.DELETE_STATUS IS NULL OR v.DELETE_STATUS = 'N') \r\n"
				+ "order by VOUCHER_ID DESC) single_voucher_detail_view\r\n"
				+ "inner join workflow_task wt on wt.reference_id=single_voucher_detail_view.VOUCHER_ID ORDER BY VOUCHER_ID DESC;";

		SingleVoucherDto singleVoucherDto = null;
		List<SingleVoucherDto> listOfSingleExpense = new ArrayList<SingleVoucherDto>();
		try (
				// Establish a connection
				Connection connection = DriverManager.getConnection(url, user, password);

				// Create a prepared statement
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			// Set the parameter value
			preparedStatement.setInt(1, employeeId);
//			preparedStatement.setInt(2, employeeId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				singleVoucherDto = new SingleVoucherDto();
				while (resultSet.next()) {

					singleVoucherDto = new SingleVoucherDto();

					singleVoucherDto.setVoucherID(resultSet.getString("VOUCHER_ID"));
					singleVoucherDto.setVoucherDate(resultSet.getString("VOUCHER_DATE"));
					singleVoucherDto.setAmount(resultSet.getString("TOTAL_AMOUNT"));
					singleVoucherDto.setBillType(resultSet.getString("INVOICE_TYPE"));
					singleVoucherDto.setExpenseStatus(resultSet.getString("EXPENSE_STATUS"));
					singleVoucherDto.setListOfProjects(resultSet.getString("projects") != null
							? Arrays.asList(resultSet.getString("projects").split(","))
							: new ArrayList<>());
					singleVoucherDto.setListOfCosts(resultSet.getString("costs") != null
							? Arrays.asList(resultSet.getString("costs").split(","))
							: new ArrayList<>());

					listOfSingleExpense.add(singleVoucherDto);
				}

			} catch (SQLException e) {
				// TODO: handle exception
				throw new SQLException("Something Went Wrong...");
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}

		return listOfSingleExpense;
	}

}
