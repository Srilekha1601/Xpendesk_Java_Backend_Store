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

import com.invoiceprocessing.invoiceprocessor.response.NotificationDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Value("${xpendesk.db.url}")
	private String url;

	@Value("${xpendesk.db.user}")
	private String user;

	@Value("${xpendesk.db.password}")
	private String password;

	@Override
	public List<NotificationDto> getNotification(NotificationDto notificationDto) {
		// TODO Auto-generated method stub

		List<NotificationDto> listOfNotificationDto = new ArrayList<NotificationDto>();
		NotificationDto notificationDtoObj = null;

		String query = "SELECT trip.TRIP_NAME, e.EMPLOYEE_NAME as SUBMITTED_BY, trip.FROM_DATE, g.GRADE as GRADE,\r\n"
				+ "CASE \r\n" + "when workflow_task.reference_type = \"T\" then trip.TRIP_ID\r\n"
				+ "when workflow_task.reference_type = \"V\" then voucher.VOUCHER_ID\r\n" + "END AS REF_ID,\r\n"
				+ "CASE\r\n" + "when workflow_task.reference_type = \"T\" then \"Trip\"\r\n"
				+ "when workflow_task.reference_type = \"V\" then \"Non-Trip\"\r\n" + "END AS REF_TYPE,\r\n"
				+ "CASE\r\n" + "when workflow_task.reference_type = \"T\" then null\r\n"
				+ "when workflow_task.reference_type = \"V\" then voucher.INVOICE_TYPE\r\n" + "END AS EXPENSE_TYPE,\r\n"
				+ "gen_amt.AMOUNT as AMOUNT, workflow_task.task_id as TASK_ID,\r\n"
				+ "workflow_task.step_id as STEP_SEQUENCE,\r\n"
				+ "workflow_task.task_description as TASK_DESCRIPTION,\r\n"
				+ "workflow_task.action_type as ACTION_TYPE,\r\n" + "CASE\r\n"
				+ "when workflow_task.action_type = \"B\" then workflow_task.comments\r\n"
				+ "when workflow_task.action_type = \"R\" then workflow_task.comments\r\n" + "END AS COMMENTS\r\n"
				+ "FROM\r\n" + "workflow_task\r\n"
				+ "LEFT JOIN trip ON workflow_task.reference_type = 'T' AND workflow_task.reference_id = trip.trip_id\r\n"
				+ "LEFT JOIN voucher ON workflow_task.reference_type = 'V' AND workflow_task.reference_id = voucher.voucher_id\r\n"
				+ "INNER JOIN employee e on e.EMPLOYEE_ID = workflow_task.generated_by\r\n"
				+ "INNER JOIN grade g on g.GRADE_ID = e.GRADE_ID\r\n" + "INNER JOIN \r\n" + "(SELECT * \r\n"
				+ "from(\r\n" + "(SELECT v.VOUCHER_ID AS REF_ID, v.TOTAL_AMOUNT AS AMOUNT\r\n" + "FROM voucher v\r\n"
				+ "WHERE v.SINGLE_EXPENSE = \"Y\")\r\n" + "union all\r\n"
				+ "(select c.TRIP_ID AS REF_ID, sum(c.AMOUNT) as AMOUNT\r\n" + "from consolidatedbreakagesummary c\r\n"
				+ "group by c.TRIP_ID)\r\n" + ") CONS) gen_amt\r\n"
				+ "on workflow_task.reference_id = gen_amt.REF_ID\r\n" + "WHERE\r\n"
				+ "((workflow_task.generated_by = ? and workflow_task.mark_as_read ='N') or (workflow_task.assigned_to = ? and workflow_task.mark_as_read is null)) "
				+ "and workflow_task.reference_id is not null;";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, notificationDto.getEmployeeId());
			preparedStatement.setInt(2, notificationDto.getEmployeeId());
			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				while (resultSet.next()) {
					notificationDtoObj = new NotificationDto();
					notificationDtoObj.setTripName(resultSet.getString("TRIP_NAME"));
					notificationDtoObj.setSubmittedBy(resultSet.getString("SUBMITTED_BY"));
					notificationDtoObj.setGrade(resultSet.getString("GRADE"));
					notificationDtoObj.setReferenceType(resultSet.getString("REF_TYPE"));
					notificationDtoObj.setExpenseType(resultSet.getString("EXPENSE_TYPE"));
					notificationDtoObj.setReferenceId(resultSet.getString("REF_ID"));
					notificationDtoObj.setTaskId(resultSet.getString("TASK_ID"));
					notificationDtoObj.setStepSequence(resultSet.getString("STEP_SEQUENCE"));
					notificationDtoObj.setAmount(resultSet.getString("AMOUNT"));
					notificationDtoObj.setTaskDescription(resultSet.getString("TASK_DESCRIPTION"));
					notificationDtoObj.setActionType(resultSet.getString("ACTION_TYPE"));
					notificationDtoObj.setComments(resultSet.getString("COMMENTS"));

					listOfNotificationDto.add(notificationDtoObj);

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		sorted by its task id..
//		listOfNotificationDto = listOfNotificationDto.stream()
//				.sorted(Comparator.comparingInt(notificationObj -> Integer.parseInt(notificationObj.getTaskId())))
//				.collect(Collectors.toList());

		listOfNotificationDto = listOfNotificationDto.stream()
				.sorted(Comparator.comparing(NotificationDto::getTaskId).reversed()).collect(Collectors.toList());

//		listOfNotificationDto.sort(Comparator.comparing(NotificationDto::getTaskId).reversed());
		return listOfNotificationDto;
	}

}
