package com.invoiceprocessing.invoiceprocessor.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.response.ApprovalDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.WorkflowApprovalService;

@Service
public class ApprovalServiceImpl implements WorkflowApprovalService {

	@Value("${xpendesk.db.url}")
	private String url;

	@Value("${xpendesk.db.user}")
	private String user;

	@Value("${xpendesk.db.password}")
	private String password;

	@Override
	public List<ApprovalDto> approvalRequests(ApprovalDto approvalDto) {
		// TODO Auto-generated method stub

		List<ApprovalDto> listOfApprovalDtoList = new ArrayList<ApprovalDto>();
		ApprovalDto approvalDtoObj = null;

		String query = "SELECT trip.TRIP_NAME, e.EMPLOYEE_NAME as SUBMITTED_BY, trip.FROM_DATE, g.GRADE as GRADE,\r\n"
				+ "CASE \r\n" + "when workflow_task.reference_type = \"T\" then trip.TRIP_ID\r\n"
				+ "when workflow_task.reference_type = \"V\" then voucher.VOUCHER_ID\r\n" + "END AS REF_ID,\r\n"
				+ "CASE\r\n" + "when workflow_task.reference_type = \"T\" then \"Trip\"\r\n"
				+ "when workflow_task.reference_type = \"V\" then \"Non-Trip\"\r\n" + "END AS REF_TYPE,\r\n"
				+ "CASE\r\n" + "when workflow_task.reference_type = \"T\" then null\r\n"
				+ "when workflow_task.reference_type = \"V\" then voucher.INVOICE_TYPE\r\n" + "END AS EXPENSE_TYPE,\r\n"
				+ "gen_amt.AMOUNT as AMOUNT, workflow_task.task_id as TASK_ID,\r\n"
				+ "CASE when workflow_task.reference_type = \"V\" then voucher.VOUCHER_DATE END as VOUCHER_DATE,\r\n"
				+ "workflow_task.step_id as STEP_SEQUENCE\r\n" + "FROM\r\n" + "workflow_task\r\n"
				+ "LEFT JOIN trip ON workflow_task.reference_type = 'T' AND workflow_task.reference_id = trip.trip_id\r\n"
				+ "LEFT JOIN voucher ON workflow_task.reference_type = 'V' AND workflow_task.reference_id = voucher.voucher_id\r\n"
				+ "INNER JOIN employee e on e.EMPLOYEE_ID = workflow_task.generated_by\r\n"
				+ "INNER JOIN grade g on g.GRADE_ID = e.GRADE_ID\r\n" + "INNER JOIN \r\n" + "(SELECT * \r\n"
				+ "from(\r\n" + "(SELECT v.VOUCHER_ID AS REF_ID, v.TOTAL_AMOUNT AS AMOUNT\r\n" + "FROM voucher v\r\n"
				+ "WHERE v.SINGLE_EXPENSE = \"Y\")\r\n" + "union all\r\n"
				+ "(select c.TRIP_ID AS REF_ID, sum(c.CLAIMED_AMOUNT) as AMOUNT\r\n"
				+ "from consolidatedbreakagesummary c\r\n" + "group by c.TRIP_ID)\r\n" + ") CONS) gen_amt\r\n"
				+ "on workflow_task.reference_id = gen_amt.REF_ID\r\n" + "WHERE\r\n"
				+ "    workflow_task.assigned_to = ? and workflow_task.action_type='A' and workflow_task.reference_id is not null;\r\n"
				+ "";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, approvalDto.getEmployeeId());
			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				while (resultSet.next()) {
					approvalDtoObj = new ApprovalDto();
					approvalDtoObj.setTripName(resultSet.getString("TRIP_NAME"));
					approvalDtoObj.setSubmittedBy(resultSet.getString("SUBMITTED_BY"));
					approvalDtoObj.setGradeId(resultSet.getString("GRADE"));
					approvalDtoObj.setReferenceType(resultSet.getString("REF_TYPE"));
					approvalDtoObj.setExpenseType(resultSet.getString("EXPENSE_TYPE"));
					approvalDtoObj.setReferenceId(resultSet.getString("REF_ID"));
					approvalDtoObj.setTaskId(resultSet.getString("TASK_ID"));
					approvalDtoObj.setStepSequence(resultSet.getString("STEP_SEQUENCE"));
					approvalDtoObj.setAmount(resultSet.getString("AMOUNT"));
					approvalDtoObj.setDate(
							resultSet.getString("REF_TYPE").equalsIgnoreCase("Trip") ? resultSet.getString("FROM_DATE")
									: resultSet.getString("VOUCHER_DATE"));

					listOfApprovalDtoList.add(approvalDtoObj);

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		sort according to the descending order ..
		listOfApprovalDtoList = listOfApprovalDtoList.stream()
				.sorted(Comparator.comparing(ApprovalDto::getTaskId).reversed()).collect(Collectors.toList());

		return listOfApprovalDtoList;
	}

}
