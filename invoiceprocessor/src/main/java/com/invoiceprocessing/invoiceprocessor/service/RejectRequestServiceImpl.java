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

import com.invoiceprocessing.invoiceprocessor.response.RejectRequestDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.RejectionRequestService;

@Service
public class RejectRequestServiceImpl implements RejectionRequestService {

	@Value("${xpendesk.db.url}")
	private String url;

	@Value("${xpendesk.db.user}")
	private String user;

	@Value("${xpendesk.db.password}")
	private String password;

	@Override
	public List<RejectRequestDto> rejectRequest(RejectRequestDto rejectRequestDto) {
		// TODO Auto-generated method stub

		List<RejectRequestDto> listOfRejectRequestDtoList = new ArrayList<RejectRequestDto>();
		RejectRequestDto rejectRequestDtoObj = null;

		String query = "SELECT trip.TRIP_NAME, e.EMPLOYEE_NAME as SUBMITTED_BY, trip.FROM_DATE, g.GRADE as GRADE,\r\n"
				+ "CASE when workflow_task.reference_type = \"T\" then trip.TRIP_ID\r\n"
				+ "when workflow_task.reference_type = \"V\" then voucher.VOUCHER_ID END AS REF_ID,\r\n"
				+ "CASE when workflow_task.reference_type = \"T\" then \"Trip\"\r\n"
				+ "when workflow_task.reference_type = \"V\" then \"Non-Trip\" END AS REF_TYPE,\r\n"
				+ "CASE when workflow_task.reference_type = \"T\" then null\r\n"
				+ "when workflow_task.reference_type = \"V\" then voucher.INVOICE_TYPE END AS EXPENSE_TYPE,\r\n"
				+ "CASE when workflow_task.reference_type = \"V\" then voucher.VOUCHER_DATE END as VOUCHER_DATE,\r\n"
				+ "gen_amt.AMOUNT as AMOUNT, workflow_task.task_id as TASK_ID,\r\n"
				+ "workflow_task.comments as COMMENTS,\r\n"
				+ "workflow_task.step_id as STEP_SEQUENCE FROM workflow_task\r\n"
				+ "LEFT JOIN trip ON workflow_task.reference_type = 'T' AND workflow_task.reference_id = trip.trip_id\r\n"
				+ "LEFT JOIN voucher ON workflow_task.reference_type = 'V' AND workflow_task.reference_id = voucher.voucher_id\r\n"
				+ "INNER JOIN employee e on e.EMPLOYEE_ID = workflow_task.generated_by\r\n"
				+ "INNER JOIN grade g on g.GRADE_ID = e.GRADE_ID INNER JOIN (SELECT * \r\n"
				+ "from((SELECT v.VOUCHER_ID AS REF_ID, v.TOTAL_AMOUNT AS AMOUNT FROM voucher v\r\n"
				+ "WHERE v.SINGLE_EXPENSE = \"Y\") union all\r\n"
				+ "(select c.TRIP_ID AS REF_ID, sum(c.CLAIMED_AMOUNT) as AMOUNT\r\n"
				+ "from consolidatedbreakagesummary c group by c.TRIP_ID)) CONS) gen_amt\r\n"
				+ "on workflow_task.reference_id = gen_amt.REF_ID WHERE\r\n"
				+ "workflow_task.assigned_to = ? and workflow_task.action_type='R' and workflow_task.reference_id is not null\r\n"
				+ "order by task_id desc;";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, rejectRequestDto.getEmployeeId());
			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				while (resultSet.next()) {
					rejectRequestDtoObj = new RejectRequestDto();
					rejectRequestDtoObj.setTripName(resultSet.getString("TRIP_NAME"));
					rejectRequestDtoObj.setSubmittedBy(resultSet.getString("SUBMITTED_BY"));
					rejectRequestDtoObj.setGradeId(resultSet.getString("GRADE"));
					rejectRequestDtoObj.setReferenceType(resultSet.getString("REF_TYPE"));
					rejectRequestDtoObj.setExpenseType(resultSet.getString("EXPENSE_TYPE"));
					rejectRequestDtoObj.setReferenceId(resultSet.getString("REF_ID"));
					rejectRequestDtoObj.setTaskId(resultSet.getString("TASK_ID"));
					rejectRequestDtoObj.setStepSequence(resultSet.getString("STEP_SEQUENCE"));
					rejectRequestDtoObj.setAmount(resultSet.getString("AMOUNT"));
					rejectRequestDtoObj.setComments(resultSet.getString("COMMENTS"));
					rejectRequestDtoObj.setDate(
							resultSet.getString("REF_TYPE").equalsIgnoreCase("Trip") ? resultSet.getString("FROM_DATE")
									: resultSet.getString("VOUCHER_DATE"));

					listOfRejectRequestDtoList.add(rejectRequestDtoObj);

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		sort the list in descending order ..
		listOfRejectRequestDtoList = listOfRejectRequestDtoList.stream()
				.sorted(Comparator.comparing(RejectRequestDto::getTaskId).reversed()).collect(Collectors.toList());

		return listOfRejectRequestDtoList;
	}

}
