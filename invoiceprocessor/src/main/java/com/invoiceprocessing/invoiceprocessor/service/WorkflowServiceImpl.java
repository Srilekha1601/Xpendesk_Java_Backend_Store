package com.invoiceprocessing.invoiceprocessor.service;

import static com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants.APPROVED;
import static com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants.IN_WORKFLOW;
import static com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants.MARK_AS_READ;
import static com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants.REJECTED;
import static com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants.SENT_BACK;
import static com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants.SUBMITTED;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.model.Employee;
import com.invoiceprocessing.invoiceprocessor.model.EmployeeRole;
import com.invoiceprocessing.invoiceprocessor.model.Role;
import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.model.Workflow;
import com.invoiceprocessing.invoiceprocessor.model.WorkflowStep;
import com.invoiceprocessing.invoiceprocessor.model.WorkflowTask;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRoleRepository;
import com.invoiceprocessing.invoiceprocessor.repository.RoleRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.WorkflowStepRepository;
import com.invoiceprocessing.invoiceprocessor.repository.WorkflowTaskRepository;
import com.invoiceprocessing.invoiceprocessor.response.CostAndProjectDetailsCheckDto;
import com.invoiceprocessing.invoiceprocessor.response.CostDetailsInfoDto;
import com.invoiceprocessing.invoiceprocessor.response.PendingTaskDto;
import com.invoiceprocessing.invoiceprocessor.response.ProjectDetailsInfoDto;
import com.invoiceprocessing.invoiceprocessor.response.ReferenceDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.WorkflowTaskDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.WorkFlowService;
import com.invoiceprocessing.invoiceprocessor.utils.XpendeskUtils;

@Service
public class WorkflowServiceImpl implements WorkFlowService {

	@Value("${xpendesk.db.url}")
	private String url;

	@Value("${xpendesk.db.user}")
	private String user;

	@Value("${xpendesk.db.password}")
	private String password;

	@Autowired
	WorkflowTaskRepository workflowTaskRepository;

	@Autowired
	TripRepository tripRepository;

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	WorkflowStepRepository workflowStepRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	EmployeeRoleRepository employeeRoleRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	EmailServiceImpl emailServiceImpl;

	@Override
	public WorkflowTaskDto processNextWorkflowTask(WorkflowTaskDto workFlowTaskDto) {

		Employee employee = employeeRepository.findByEmployeeId(XpendeskUtils.extractLoggedInUserId()).get();

//		For current task, update executed_by, executed_on, action, comments
		WorkflowTask currentWorkflowTask = updateCurrentTaskForWorkflow(workFlowTaskDto, employee);
		WorkflowTask newWorkflowTask = null;
		WorkflowTaskDto workflowTaskDto = new WorkflowTaskDto();
//		MailBeanDto mailSend = new MailBeanDto();
		if (workFlowTaskDto.getActionType().equalsIgnoreCase(SENT_BACK)) {
			ReferenceDto reference = new ReferenceDto();
			reference.setReferenceId(workFlowTaskDto.getReferenceID());
			reference.setReferenceType(workFlowTaskDto.getReferenceType());
			newWorkflowTask = processFirstWorkflowTask(reference);
			workflowTaskDto = convertEntityToModel(newWorkflowTask, workflowTaskDto);
			updateReferenceStatus(workFlowTaskDto.getReferenceType(), workFlowTaskDto.getReferenceID(),
					workFlowTaskDto.getActionType());
		} else if (workFlowTaskDto.getActionType().equalsIgnoreCase(REJECTED)) {
			updateReferenceStatus(workFlowTaskDto.getReferenceType(), workFlowTaskDto.getReferenceID(),
					workFlowTaskDto.getActionType());
//			mailSend.setFrom(employee.getEmailID());
//			mailSend.setBody("This Request is Rejected..");
//			emailServiceImpl.sendEmail(mailSend);
		} else if (workFlowTaskDto.getActionType().equalsIgnoreCase(APPROVED)) {
			// Check if next workflow step exists
			Integer stepSequence = workFlowTaskDto.getStepSequence() + 1;
			WorkflowStep workflowStep = getNextStepInWorkflow(
					workflowTaskRepository.findById(workFlowTaskDto.getTaskID()).get().getStep().getWorkflow(),
					stepSequence);

			Integer refId = workFlowTaskDto.getReferenceID();
			String refType = workFlowTaskDto.getReferenceType();

			// Get the trip / voucher amount to be approved
			BigDecimal workflowAmount = new BigDecimal(0);
			switch (refType.toUpperCase()) {
			case "T": {
				Optional<Trip> tripOptional = tripRepository.findById(workFlowTaskDto.getReferenceID());
				List<Voucher> voucherList = tripOptional.isPresent() ? tripOptional.get().getVouchers()
						: Collections.emptyList();

				workflowAmount = voucherList.stream().map(Voucher::getClaimedAmount).reduce(BigDecimal.ZERO,
						BigDecimal::add);
//				System.out.println("Workflow amount");
//				System.out.println(workflowAmount);
				break;
			}
			case "V": {
				Optional<Voucher> voucherOptional = voucherRepository.findById(workFlowTaskDto.getReferenceID());
				Voucher voucher = voucherOptional.isPresent() ? voucherOptional.get() : voucherOptional.orElse(null);

				workflowAmount = voucher.getClaimedAmount();
				break;
			}
			}

			// Keep on checking if next task needs to be created based on certain conditions
			// Currently only the total trip / voucher amount is checked against a threshold
			// In future more checks may come in
			while (workflowStep != null) {
				if (workflowStep.getVoucherAmountThreshold() != null
						&& workflowStep.getVoucherAmountThreshold().compareTo(workflowAmount) >= 0)
					workflowStep = getNextStepInWorkflow(
							workflowTaskRepository.findById(workFlowTaskDto.getTaskID()).get().getStep().getWorkflow(),
							++stepSequence);
				else
					break;
			}

			// If exists, create a new task based on this step and update the trip / voucher
			// status to 'In workflow'.
			// Else just update the trip / voucher status to Approved
			if (workflowStep == null) {
				updateReferenceStatus(workFlowTaskDto.getReferenceType(), workFlowTaskDto.getReferenceID(),
						workFlowTaskDto.getActionType());
//				mailSend.setFrom(employee.getEmailID());
//				mailSend.setBody("This Request is Approved..");
//				emailServiceImpl.sendEmail(mailSend);
			} else {
				ReferenceDto reference = new ReferenceDto();
				reference.setReferenceId(refId);
				reference.setReferenceType(refType);
				reference.setEmployee(currentWorkflowTask.getGeneratedBy());
				newWorkflowTask = createNextTaskForWorkflow(workflowStep, employee, reference);
				workflowTaskDto = convertEntityToModel(newWorkflowTask, workflowTaskDto);
				updateReferenceStatus(workFlowTaskDto.getReferenceType(), workFlowTaskDto.getReferenceID(),
						IN_WORKFLOW);
			}
		}
//		System.out.println("The workflowdto");
//		System.out.println(workflowTaskDto);
		return workflowTaskDto;
	}

	@Override
	public WorkflowTask processFirstWorkflowTask(ReferenceDto reference) {

		Employee employee = employeeRepository.findByEmployeeId(XpendeskUtils.extractLoggedInUserId()).get();
		WorkflowStep workflowStep = getNextStepInWorkflow(employee.getGradeID().getWorkflow(), 1);

//		If exists, create the first task based on this step
		if (workflowStep != null) {
			return createNextTaskForWorkflow(workflowStep, employee, reference);
		} else { // else part are added.... (newly added....)//////
			updateReferenceStatus(reference.getReferenceType(), reference.getReferenceId(), APPROVED);
		}
		return null;
	}

	private void updateReferenceStatus(String referenceType, Integer referenceId, String actionType) {
		// Update status of the reference
		switch (referenceType.toUpperCase()) {
		case "T": {
			Trip trip = tripRepository.findById(referenceId).get();
			trip.setExpenseStatus(actionType);
			tripRepository.save(trip);
			break;
		}
		case "V": {
			Voucher voucher = voucherRepository.findById(referenceId).get();
			voucher.setExpenseStatus(actionType);
			voucherRepository.save(voucher);
			break;
		}
		default: {
		}
		}
	}

	private WorkflowTask createNextTaskForWorkflow(WorkflowStep workflowStep, Employee employee,
			ReferenceDto reference) {

		List<WorkflowTask> workflowTaskList = workflowTaskRepository
				.findAllByReferenceIDOrderByTaskIdDesc(reference.getReferenceId());

		WorkflowTask newTask = new WorkflowTask();
		newTask.setTaskDescription(createTaskDescription(workflowStep, employee));
		if (workflowStep.getStepSequence() == 1)
			newTask.setGeneratedBy(employee);
		else
			newTask.setGeneratedBy(reference.getEmployee());

		if (!workflowTaskList.isEmpty() && workflowTaskList.stream().findFirst().get().getApprovalId() == null) {
			Random random = new Random();
			int approvalId = random.nextInt(9999);
			newTask.setApprovalId(String.valueOf(approvalId));
		} else {
			if (workflowTaskList.stream().findFirst().isPresent()) {
				newTask.setApprovalId(workflowTaskList.stream().findFirst().get().getApprovalId());
				workflowTaskList.stream().forEach(workflowTask -> {
					workflowTask.setApprovalId(workflowTaskList.stream().findFirst().get().getApprovalId());
				});
				workflowTaskRepository.saveAll(workflowTaskList);
			}
		}
//		newTask.setGeneratedOn(Timestamp.valueOf(new Date().toString()));
//		Initial implementation: assume the task is assigned to only one employee. In next step we will implement
//		assigning to multiple employee based on multiple roles defined in step
		Employee assignedTo = getAssignedToList(Arrays.asList(workflowStep.getApproverRole()), employee).stream()
				.findFirst().orElse(null);
		newTask.setAssignedTo(assignedTo);
		newTask.setStep(workflowStep);
		newTask.setReferenceID(reference.getReferenceId());
		newTask.setReferenceType(reference.getReferenceType());
		newTask.setActionType(SUBMITTED);
		return workflowTaskRepository.save(newTask);

	}

	private WorkflowTask updateCurrentTaskForWorkflow(WorkflowTaskDto workFlowTaskDto, Employee employee) {
		WorkflowTask currentTask = workflowTaskRepository.findById(workFlowTaskDto.getTaskID()).get();
		currentTask.setExecutedBy(employee);
//		currentTask.setExecutedOn(Timestamp.valueOf(new Date().toString()));
		currentTask.setActionType(workFlowTaskDto.getActionType());
		currentTask.setComments(workFlowTaskDto.getComments());
		currentTask.setMarkAsRead(MARK_AS_READ);
		return workflowTaskRepository.save(currentTask);
	}

	private String createTaskDescription(WorkflowStep workflowStep, Employee employee) {
		StringBuilder desc = new StringBuilder(StringUtils.EMPTY);
		desc.append(workflowStep.getStepName());
		desc.append(" by ");
		desc.append(employee.getName());
		return desc.toString();
	}

	private WorkflowStep getNextStepInWorkflow(Workflow workflow, Integer sequence) {
		if (workflow == null)
			return null;
		return workflowStepRepository.findByWorkflowAndStepSequence(workflow, sequence).stream().findFirst()
				.orElse(null);
	}

	private List<Employee> getAssignedToList(List<Role> assinedToRoleList, Employee employee) {

		List<Employee> assignedToList = new ArrayList<Employee>();
		for (Role role : assinedToRoleList) {
			switch (role.getRoleDescription().toUpperCase()) {
			case "ROLE_MANAGER": {
				assignedToList.add(employee.getManager());
				break;
			}
			case "ROLE_PARTNER": {
				assignedToList.add(employee.getPartner());
				break;
			}
			case "ROLE_ACCOUNTS": {
				Role roleEntity = roleRepository.findByRoleDescription(role.getRoleDescription().toUpperCase());
				EmployeeRole employeeRole = employeeRoleRepository.findByRoleID(roleEntity).stream().findFirst().get();
				assignedToList.add(employeeRole.getEmpliyeeID());
				break;
			}
			default: {
			}
			}
		}
		return assignedToList;
	}

	@Override
	public List<WorkflowTaskDto> getWorkflowTasks(WorkflowTaskDto workFlowTaskDto) {
		List<WorkflowTask> workflowTaskList = workflowTaskRepository.findByActionType(workFlowTaskDto.getActionType());

		List<WorkflowTaskDto> workflowTaskDtoList = new ArrayList<WorkflowTaskDto>();
		// Populate List<WorkflowTaskDto> from workflowTaskList

		return workflowTaskDtoList;
	}

	@Override
	public List<PendingTaskDto> pendingTasksForWorkflow(PendingTaskDto pendingTaskDto) {
//		String url = "jdbc:mysql://localhost:3306/expendesk";
//		String user = "root";
//		String password = "#Sroy@1010";

		List<PendingTaskDto> listOfPendingTaskDtoList = new ArrayList<PendingTaskDto>();
		PendingTaskDto pendingDto = null;

		String query = "WITH LiquorExists AS (\n" +
			    "    SELECT \n" +
			    "        CASE \n" +
			    "            WHEN MAX(CASE WHEN fv.CONTAINS_LIQUOR = '0' THEN 'F' ELSE 'T' END) = 'T' THEN 'T'\n" +
			    "            ELSE 'F'\n" +
			    "        END AS LIQUOR_STATUS\n" +
			    "    FROM \n" +
			    "        consolidatedbreakagesummary c, \n" +
			    "        voucher v, \n" +
			    "        food_voucher fv\n" +
			    "    WHERE \n" +
			    "        c.TRIP_ID = v.TRIP_ID AND \n" +
			    "        v.VOUCHER_ID = fv.VOUCHER_ID\n" +
			    ")\n" +
			    "SELECT \n" +
			    "    trip.TRIP_NAME, \n" +
			    "    e.EMPLOYEE_NAME AS SUBMITTED_BY, \n" +
			    "    CASE\n" +
			    "        WHEN workflow_task.reference_type = 'T' THEN trip.FROM_DATE\n" +
			    "        WHEN workflow_task.reference_type = 'V' THEN voucher.VOUCHER_DATE\n" +
			    "    END AS SUBMISSION_DATE, \n" +
			    "    g.GRADE AS GRADE, \n" +
			    "    CASE \n" +
			    "        WHEN workflow_task.reference_type = 'T' THEN trip.TRIP_ID\n" +
			    "        WHEN workflow_task.reference_type = 'V' THEN voucher.VOUCHER_ID\n" +
			    "    END AS REF_ID, \n" +
			    "    CASE \n" +
			    "        WHEN workflow_task.reference_type = 'T' THEN 'Trip'\n" +
			    "        WHEN workflow_task.reference_type = 'V' THEN 'Non-Trip' \n" +
			    "    END AS REF_TYPE,\n" +
			    "    CASE \n" +
			    "        WHEN workflow_task.reference_type = 'T' THEN NULL\n" +
			    "        WHEN workflow_task.reference_type = 'V' THEN voucher.INVOICE_TYPE\n" +
			    "    END AS EXPENSE_TYPE, \n" +
			    "    gen_amt.AMOUNT AS AMOUNT,\n" +
			    "    trip.APPROVED_AMOUNT AS APPROVED_AMOUNT, " +
			    "	trip.FROM_DATE as FROM_DATE	,"+
			    "	trip.TO_DATE as TO_DATE	,"+
			    "    workflow_task.task_id AS TASK_ID, \n" +
			    "    ws.step_sequence AS STEP_SEQUENCE,\n" +
			    "    CASE\n" +
			    "        WHEN MAX(CASE WHEN c.EXCEPTION_REASON IS NULL OR c.EXCEPTION_REASON = '' THEN 'F' ELSE 'T' END) = 'T' THEN 'T'\n" +
			    "        ELSE 'F' \n" +
			    "    END AS EXCEPTION_REASON_EXISTS, \n" +
			    "    le.LIQUOR_STATUS\n" +
			    "FROM \n" +
			    "    workflow_task\n" +
			    "LEFT JOIN \n" +
			    "    trip ON workflow_task.reference_type = 'T' AND workflow_task.reference_id = trip.trip_id\n" +
			    "LEFT JOIN \n" +
			    "    voucher ON workflow_task.reference_type = 'V' AND workflow_task.reference_id = voucher.voucher_id\n" +
			    "INNER JOIN \n" +
			    "    employee e ON e.EMPLOYEE_ID = workflow_task.generated_by\n" +
			    "INNER JOIN \n" +
			    "    grade g ON g.GRADE_ID = e.GRADE_ID \n" +
			    "INNER JOIN (\n" +
			    "    SELECT * \n" +
			    "    FROM (\n" +
			    "        SELECT \n" +
			    "            v.VOUCHER_ID AS REF_ID, \n" +
			    "            v.TOTAL_AMOUNT AS AMOUNT\n" +
			    "        FROM \n" +
			    "            voucher v \n" +
			    "        WHERE \n" +
			    "            v.SINGLE_EXPENSE = 'Y' \n" +
			    "        UNION ALL \n" +
			    "        SELECT \n" +
			    "            c.TRIP_ID AS REF_ID, \n" +
			    "            SUM(c.CLAIMED_AMOUNT) AS AMOUNT\n" +
			    "        FROM \n" +
			    "            consolidatedbreakagesummary c \n" +
			    "        GROUP BY \n" +
			    "            c.TRIP_ID\n" +
			    "    ) CONS\n" +
			    ") gen_amt ON workflow_task.reference_id = gen_amt.REF_ID\n" +
			    "INNER JOIN \n" +
			    "    workflow_step ws ON ws.step_id = workflow_task.step_id \n" +
			    "LEFT JOIN \n" +
			    "    consolidatedbreakagesummary c ON c.TRIP_ID = trip.TRIP_ID\n" +
			    "CROSS JOIN \n" +
			    "    LiquorExists le \n" +
			    "WHERE \n" +
			    "    workflow_task.assigned_to = ?\n" +
			    "    AND workflow_task.action_type = 'S' \n" +
			    "    AND workflow_task.approval_id IS NULL \n" +
			    "    AND workflow_task.reference_id IS NOT NULL\n" +
			    "    AND workflow_task.reference_type IN ('T', 'V')\n" +
			    "GROUP BY \n" +
			    "    trip.TRIP_NAME, \n" +
			    "    e.EMPLOYEE_NAME, \n" +
			    "    trip.FROM_DATE, \n" +
			    "    g.GRADE, \n" +
			    "    REF_ID, \n" +
			    "    REF_TYPE, \n" +
			    "    EXPENSE_TYPE, \n" +
			    "    gen_amt.AMOUNT, \n" +
			    "    trip.FROM_DATE  , \n" +
			    "    trip.TO_DATE  , \n" +
			    "    workflow_task.task_id, \n" +
			    "    ws.step_sequence, \n" +
			    "    le.LIQUOR_STATUS\n" +
			    "ORDER BY workflow_task.task_id DESC;";


		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, pendingTaskDto.getEmployeeId());
			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				while (resultSet.next()) {
					pendingDto = new PendingTaskDto();
					pendingDto.setTripName(resultSet.getString("TRIP_NAME"));
					pendingDto.setSubmittedBy(resultSet.getString("SUBMITTED_BY"));
					pendingDto.setGradeId(resultSet.getString("GRADE"));
					pendingDto.setReferenceType(resultSet.getString("REF_TYPE"));
					pendingDto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("SUBMISSION_DATE")));
					pendingDto.setExpenseType(resultSet.getString("EXPENSE_TYPE"));
					pendingDto.setReferenceId(resultSet.getString("REF_ID"));
					pendingDto.setTaskId(resultSet.getString("TASK_ID"));
					pendingDto.setStepSequence(resultSet.getString("STEP_SEQUENCE"));
					pendingDto.setAmount(resultSet.getString("AMOUNT"));
					pendingDto.setLiquorStatus(resultSet.getString("LIQUOR_STATUS"));
					pendingDto.setExceptionReasonExists(resultSet.getString("EXCEPTION_REASON_EXISTS"));
					pendingDto.setFromDate(resultSet.getString("FROM_DATE"));
					pendingDto.setToDate(resultSet.getString("TO_DATE"));

					listOfPendingTaskDtoList.add(pendingDto);

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		sort the list based on task id 
//		listOfPendingTaskDtoList = listOfPendingTaskDtoList.stream()
//				.sorted(Comparator.comparing(PendingTaskDto::getTaskId).reversed()).collect(Collectors.toList());
//		System.out.println();
		return listOfPendingTaskDtoList;
	}

	private WorkflowTaskDto convertEntityToModel(WorkflowTask newWorkflowTask, WorkflowTaskDto workflowTaskDto) {
		// TODO Auto-generated method stub
		workflowTaskDto.setTaskID(newWorkflowTask.getTaskId());
		workflowTaskDto.setTaskDescription(newWorkflowTask.getTaskDescription());
		workflowTaskDto.setActionType(newWorkflowTask.getActionType());
		workflowTaskDto.setAssignedTo(newWorkflowTask.getAssignedTo().getEmployeeId());
//		workflowTaskDto.setExecutedBy(newWorkflowTask.getExecutedBy().getEmployeeID());
//		workflowTaskDto.setExecutedOn(newWorkflowTask.getExecutedOn().toString());
//		workflowTaskDto.setGeneratedBy(newWorkflowTask.getExecutedBy().getEmployeeID());
		workflowTaskDto.setReferenceID(newWorkflowTask.getReferenceID());
		workflowTaskDto.setReferenceType(newWorkflowTask.getReferenceType());
		workflowTaskDto.setStepSequence(newWorkflowTask.getStep().getStepSequence());
		workflowTaskDto.setComments(newWorkflowTask.getTaskDescription());
//		workflowTaskDto.setGeneratedOn(newWorkflowTask.getGeneratedOn().toString());

		return workflowTaskDto;
	}

	@Override
	public List<PendingTaskDto> pendingTasksForWorkflowForAnyNextLevel(PendingTaskDto pendingTaskDto) {

		List<PendingTaskDto> listOfPendingTaskDtoList = new ArrayList<PendingTaskDto>();
		PendingTaskDto pendingDto = null;

		String query = "WITH LiquorExists AS (\n" +
			    "    SELECT \n" +
			    "        CASE\n" +
			    "            WHEN MAX(CASE WHEN fv.CONTAINS_LIQUOR = '0' THEN 'F' ELSE 'T' END) = 'T' THEN 'T'\n" +
			    "            ELSE 'F'\n" +
			    "        END AS LIQUOR_STATUS\n" +
			    "    FROM \n" +
			    "        consolidatedbreakagesummary c\n" +
			    "    JOIN \n" +
			    "        voucher v ON c.TRIP_ID = v.TRIP_ID\n" +
			    "    JOIN \n" +
			    "        food_voucher fv ON v.VOUCHER_ID = fv.VOUCHER_ID\n" +
			    ")\n" +
			    "SELECT \n" +
			    "    trip.TRIP_NAME, \n" +
			    "    e.EMPLOYEE_NAME AS SUBMITTED_BY, \n" +
			    "    CASE\n" +
			    "        WHEN workflow_task.reference_type = 'T' THEN trip.FROM_DATE\n" +
			    "        WHEN workflow_task.reference_type = 'V' THEN voucher.VOUCHER_DATE\n" +
			    "    END AS SUBMISSION_DATE, \n" +
			    "    g.GRADE AS GRADE, \n" +
			    "    CASE \n" +
			    "        WHEN workflow_task.reference_type = 'T' THEN trip.TRIP_ID\n" +
			    "        WHEN workflow_task.reference_type = 'V' THEN voucher.VOUCHER_ID\n" +
			    "    END AS REF_ID, \n" +
			    "    CASE \n" +
			    "        WHEN workflow_task.reference_type = 'T' THEN 'Trip'\n" +
			    "        WHEN workflow_task.reference_type = 'V' THEN 'Non-Trip'\n" +
			    "    END AS REF_TYPE,\n" +
			    "    CASE \n" +
			    "        WHEN workflow_task.reference_type = 'T' THEN NULL\n" +
			    "        WHEN workflow_task.reference_type = 'V' THEN voucher.INVOICE_TYPE\n" +
			    "    END AS EXPENSE_TYPE, \n" +
			    "    gen_amt.AMOUNT AS AMOUNT,\n" +
			    "    trip.APPROVED_AMOUNT AS APPROVED_AMOUNT,\n" +
			    "    trip.FROM_DATE as FROM_DATE,\n" +
			    "    trip.TO_DATE as TO_DATE,\n" +
			    "    workflow_task.task_id AS TASK_ID, \n" +
			    "    ws.step_sequence AS STEP_SEQUENCE,\n" +
			    "    CASE\n" +
			    "        WHEN MAX(CASE WHEN c.EXCEPTION_REASON IS NULL OR c.EXCEPTION_REASON = '' THEN 'F' ELSE 'T' END) = 'T' THEN 'T'\n" +
			    "        ELSE 'F'\n" +
			    "    END AS EXCEPTION_REASON_EXISTS, \n" +
			    "    le.LIQUOR_STATUS,\n" +
			    "    wt2.assigned_to AS ADDITIONAL_ASSIGNEE, -- New column added\n" +
			    "    e2.EMPLOYEE_NAME AS ADDITIONAL_ASSIGNEE_NAME -- Name of the additional assignee\n" +
			    "FROM \n" +
			    "    workflow_task\n" +
			    "LEFT JOIN \n" +
			    "    trip ON workflow_task.reference_type = 'T' AND workflow_task.reference_id = trip.trip_id\n" +
			    "LEFT JOIN \n" +
			    "    voucher ON workflow_task.reference_type = 'V' AND workflow_task.reference_id = voucher.voucher_id\n" +
			    "JOIN \n" +
			    "    employee e ON e.EMPLOYEE_ID = workflow_task.generated_by\n" +
			    "JOIN \n" +
			    "    grade g ON g.GRADE_ID = e.GRADE_ID \n" +
			    "JOIN \n" +
			    "    (\n" +
			    "        SELECT * \n" +
			    "        FROM (\n" +
			    "            SELECT v.VOUCHER_ID AS REF_ID, v.TOTAL_AMOUNT AS AMOUNT\n" +
			    "            FROM voucher v \n" +
			    "            WHERE v.SINGLE_EXPENSE = 'Y' \n" +
			    "            UNION ALL \n" +
			    "            SELECT c.TRIP_ID AS REF_ID, SUM(c.CLAIMED_AMOUNT) AS AMOUNT\n" +
			    "            FROM consolidatedbreakagesummary c \n" +
			    "            GROUP BY c.TRIP_ID\n" +
			    "        ) CONS\n" +
			    "    ) gen_amt ON workflow_task.reference_id = gen_amt.REF_ID\n" +
			    "JOIN \n" +
			    "    workflow_step ws ON ws.step_id = workflow_task.step_id \n" +
			    "LEFT JOIN \n" +
			    "    consolidatedbreakagesummary c ON c.TRIP_ID = trip.TRIP_ID\n" +
			    "CROSS JOIN \n" +
			    "    LiquorExists le \n" +
			    "LEFT JOIN \n" +
			    "    workflow_task wt2 ON wt2.reference_id = workflow_task.reference_id \n" +
			    "    AND wt2.task_id != workflow_task.task_id -- Ensure it's a different row\n" +
			    "LEFT JOIN \n" +
			    "    employee e2 ON e2.EMPLOYEE_ID = wt2.assigned_to -- Join to get the name of the additional assignee\n" +
			    "WHERE \n" +
			    "    workflow_task.assigned_to = ? \n" +
			    "    AND workflow_task.action_type = 'S' \n" +
			    "    AND workflow_task.approval_id IS NOT NULL \n" +
			    "    AND workflow_task.reference_id IS NOT NULL\n" +
			    "    AND workflow_task.reference_type IN ('T', 'V')\n" +
			    "GROUP BY \n" +
			    "    trip.TRIP_NAME, e.EMPLOYEE_NAME, trip.FROM_DATE, g.GRADE, REF_ID, REF_TYPE, EXPENSE_TYPE, gen_amt.AMOUNT, \n" +
			    "    trip.APPROVED_AMOUNT, trip.FROM_DATE, trip.TO_DATE, workflow_task.task_id, ws.step_sequence, le.LIQUOR_STATUS, \n" +
			    "    wt2.assigned_to, e2.EMPLOYEE_NAME\n" +
			    "ORDER BY \n" +
			    "    workflow_task.task_id DESC;";



		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, pendingTaskDto.getEmployeeId());
			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				while (resultSet.next()) {
					pendingDto = new PendingTaskDto();
					pendingDto.setTripName(resultSet.getString("TRIP_NAME"));
					pendingDto.setSubmittedBy(resultSet.getString("SUBMITTED_BY"));
					pendingDto.setGradeId(resultSet.getString("GRADE"));
					pendingDto.setReferenceType(resultSet.getString("REF_TYPE"));
					pendingDto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("SUBMISSION_DATE")));
					pendingDto.setExpenseType(resultSet.getString("EXPENSE_TYPE"));
					pendingDto.setReferenceId(resultSet.getString("REF_ID"));
					pendingDto.setTaskId(resultSet.getString("TASK_ID"));
					pendingDto.setStepSequence(resultSet.getString("STEP_SEQUENCE"));
					pendingDto.setAmount(resultSet.getString("AMOUNT"));
					pendingDto.setLiquorStatus(resultSet.getString("LIQUOR_STATUS"));
					pendingDto.setExceptionReasonExists(resultSet.getString("EXCEPTION_REASON_EXISTS"));
					pendingDto.setApprovedAmount(resultSet.getString("APPROVED_AMOUNT"));
					
					pendingDto.setFromDate(resultSet.getString("FROM_DATE"));
					pendingDto.setToDate(resultSet.getString("TO_DATE"));
					//pendingDto.setAdditionalAssignee(resultSet.get);
					pendingDto.setAdditionalAssigneeName(resultSet.getString("ADDITIONAL_ASSIGNEE_NAME"));
					listOfPendingTaskDtoList.add(pendingDto);

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

//		listOfPendingTaskDtoList = listOfPendingTaskDtoList.stream()
//				.sorted(Comparator.comparing(PendingTaskDto::getTaskId).reversed()).collect(Collectors.toList());

		return listOfPendingTaskDtoList;

	}

	@Override
	public CostAndProjectDetailsCheckDto getCostAndProjectDetails(VoucherDto voucherDto) {
		// TODO Auto-generated method stub

		CostAndProjectDetailsCheckDto costAndProjectDetailsCheckDto = new CostAndProjectDetailsCheckDto();

		ProjectDetailsInfoDto projectDetailsInfoDto = new ProjectDetailsInfoDto();

		CostDetailsInfoDto costDetailsInfoDto = new CostDetailsInfoDto();

		String query = "select v.INVOICE_TYPE, \r\n" + "v.VOUCHER_ID as VOUCHER_ID,\r\n"
				+ "(select group_concat(p.project_code) from voucher_projects vp, project p where vp.PROJECT_ID=p.project_id and vp.VOUCHER_ID=v.VOUCHER_ID) as projects,\r\n"
				+ "(select group_concat(vp.CLAIMED_AMOUNT) from voucher_projects vp, project p where vp.PROJECT_ID=p.project_id and vp.VOUCHER_ID=v.VOUCHER_ID) as project_amount,\r\n"
				+ "(select group_concat(c.cost_code) from voucher_costs vc , cost_code c where vc.cost_id=c.cost_id and vc.VOUCHER_ID=v.VOUCHER_ID) as costs,\r\n"
				+ "(select group_concat(vc.CLAIMED_AMOUNT) from voucher_costs vc , cost_code c where vc.cost_id=c.cost_id and vc.VOUCHER_ID=v.VOUCHER_ID) as cost_amount,  \r\n"
				+ "v.VOUCHER_DATE as VOUCHER_DATE,\r\n" + "v.TOTAL_AMOUNT\r\n" + "from voucher v\r\n" + "where\r\n"
				+ "v.VOUCHER_ID = ?\r\n" + "and\r\n" + "v.SINGLE_EXPENSE = \"Y\"\r\n" + "order by VOUCHER_ID DESC;";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, Integer.parseInt(voucherDto.getVoucherID()));
			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				while (resultSet.next()) {

					projectDetailsInfoDto.setProjectCode(resultSet.getString("projects") != null
							? Arrays.asList(resultSet.getString("projects").split(","))
							: new ArrayList<>());
					projectDetailsInfoDto.setAmount(resultSet.getString("project_amount") != null
							? Arrays.asList(resultSet.getString("project_amount").split(","))
							: new ArrayList<>());

					costAndProjectDetailsCheckDto.setProjectDetailsInfoDtos(projectDetailsInfoDto);

					costDetailsInfoDto.setCostCode(resultSet.getString("costs") != null
							? Arrays.asList(resultSet.getString("costs").split(","))
							: new ArrayList<>());
					costDetailsInfoDto.setAmount(resultSet.getString("cost_amount") != null
							? Arrays.asList(resultSet.getString("cost_amount").split(","))
							: new ArrayList<>());

				}

				costAndProjectDetailsCheckDto.setProjectDetailsInfoDtos(projectDetailsInfoDto);
				costAndProjectDetailsCheckDto.setCostDetailsInfoDtos(costDetailsInfoDto);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return costAndProjectDetailsCheckDto;
	}

}
