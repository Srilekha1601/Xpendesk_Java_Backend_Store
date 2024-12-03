package com.invoiceprocessing.invoiceprocessor.mapper;

import static com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants.TRIP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
//import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.invoiceprocessing.invoiceprocessor.model.ConsolidatedBreakageSummary;
import com.invoiceprocessing.invoiceprocessor.model.ConsolidatedExpenseDetail;
import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.repository.ConsolidatedExpenseDetailRepository;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.InvoiceConsolidatedSummeryRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripRepository;
import com.invoiceprocessing.invoiceprocessor.response.ConsolidatedBreakageSummaryDto;
import com.invoiceprocessing.invoiceprocessor.response.HistoryOfConsolidatedData;
import com.invoiceprocessing.invoiceprocessor.response.ReferenceDto;
import com.invoiceprocessing.invoiceprocessor.response.SingleVoucherDto;
import com.invoiceprocessing.invoiceprocessor.service.WorkflowServiceImpl;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component("consolidation")
public class ConsolidatedVoucherMapperImpl {

	@Value("${approval.required.rule.passed}")
	String approvalRequiredWhenRulePassed;

	@Value("${xpendesk.db.url}")
	private String url;

	@Value("${xpendesk.db.user}")
	private String user;

	@Value("${xpendesk.db.password}")
	private String password;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	InvoiceConsolidatedSummeryRepository ivcsRepository;

	@Autowired
	TripRepository tripRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	WorkflowServiceImpl workflowServiceImpl;

	@Autowired
	ConsolidatedExpenseDetailRepository consolidatedExpenseDetailRepository;

	@Transactional
	public List<ConsolidatedBreakageSummaryDto> modelToDto(Integer tripId) {

		// return ivcsRepo.findSummary(tripId);

		List<ConsolidatedBreakageSummaryDto> consBreakageSummaryList = new ArrayList<ConsolidatedBreakageSummaryDto>();
		List<ConsolidatedExpenseDetail> listOfConsolidatedDetails = new ArrayList<ConsolidatedExpenseDetail>();
		ConsolidatedBreakageSummaryDto consolidatedBreakageSummaryDto = null;
		ConsolidatedExpenseDetail consolidatedExpenseDetail = null;
		Integer count = 0;
		// SQL query to execute
		String query = "SELECT \r\n" + "TRIP_ID, \r\n" + "category, \r\n" + "VOUCHER_DATE, \r\n"
				+ "    SUM(AMOUNT) as AMOUNT, \r\n" + "GROUP_CONCAT(VOUCHER_ID) as VOUCHER_IDS\r\n" + "FROM (\r\n"
				+ "    SELECT \r\n" + "t.TRIP_ID as TRIP_ID, \r\n" + "hvd.BILL_TYPE as category, \r\n"
				+ "        hvd.ITEM_DATE as VOUCHER_DATE, \r\n" + "hvd.AMOUNT + hvd.GST_AMOUNT as AMOUNT,\r\n"
				+ "        v.VOUCHER_ID\r\n" + "FROM trip t\r\n" + "JOIN voucher v ON t.TRIP_ID = v.TRIP_ID\r\n"
				+ "    JOIN hotel_voucher hv ON v.VOUCHER_ID = hv.VOUCHER_ID\r\n"
				+ "    JOIN hotel_voucher_detail hvd ON hv.HOTEL_VOUCHER_ID = hvd.HOTEL_VOUCHER_ID\r\n"
				+ "    WHERE v.DELETE_STATUS IS NULL OR v.DELETE_STATUS = 'N'\r\n" + "\r\n" + "UNION ALL\r\n"
				+ "    \r\n" + "SELECT \r\n" + "t.TRIP_ID as TRIP_ID,\r\n" + "'accommodation' as category, \r\n"
				+ "v.INVOICE_DATE as VOUCHER_DATE, \r\n" + "v.CLAIMED_AMOUNT as AMOUNT,\r\n" + "v.VOUCHER_ID\r\n"
				+ "FROM trip t\r\n" + "JOIN voucher v ON t.TRIP_ID = v.TRIP_ID\r\n"
				+ "    JOIN hotel_voucher hv ON v.VOUCHER_ID = hv.VOUCHER_ID\r\n"
				+ "    WHERE v.MANUAL_ENTRY = 'Y' AND (v.DELETE_STATUS IS NULL OR v.DELETE_STATUS = 'N')\r\n"
				+ "    \r\n" + "UNION ALL\r\n" + "\r\n" + "SELECT \r\n" + "t.TRIP_ID as TRIP_ID, \r\n"
				+ "'food' as category, \r\n" + "v.VOUCHER_DATE as VOUCHER_DATE, \r\n" + "fv.TOTAL_AMOUNT as AMOUNT,\r\n"
				+ "v.VOUCHER_ID\r\n" + "FROM trip t\r\n" + "JOIN voucher v ON t.TRIP_ID = v.TRIP_ID\r\n"
				+ "    JOIN food_voucher fv ON v.VOUCHER_ID = fv.VOUCHER_ID\r\n"
				+ "    WHERE v.DELETE_STATUS IS NULL OR v.DELETE_STATUS = 'N'\r\n" + "\r\n" + "UNION ALL\r\n"
				+ "    \r\n" + "SELECT \r\n" + "t.TRIP_ID as TRIP_ID,\r\n" + "'food' as category, \r\n"
				+ "fvd.ITEM_DATE as VOUCHER_DATE, \r\n" + "fvd.AMOUNT + fvd.GST_AMOUNT as AMOUNT,\r\n"
				+ "v.VOUCHER_ID\r\n" + "FROM trip t\r\n" + "JOIN voucher v ON t.TRIP_ID = v.TRIP_ID\r\n"
				+ "    JOIN hotel_voucher hv ON v.VOUCHER_ID = hv.VOUCHER_ID\r\n"
				+ "    JOIN food_voucher_detail fvd ON hv.HOTEL_VOUCHER_ID = fvd.HOTEL_VOUCHER_ID\r\n"
				+ "    WHERE v.DELETE_STATUS IS NULL OR v.DELETE_STATUS = 'N'\r\n" + "\r\n" + "UNION ALL\r\n"
				+ "    \r\n" + "SELECT \r\n" + "t.TRIP_ID as TRIP_ID,\r\n" + "'conveyance' as category, \r\n"
				+ "v.VOUCHER_DATE as VOUCHER_DATE,\r\n" + "v.TOTAL_AMOUNT as AMOUNT,\r\n" + "v.VOUCHER_ID\r\n"
				+ "FROM trip t\r\n" + "JOIN voucher v ON t.TRIP_ID = v.TRIP_ID\r\n"
				+ "    JOIN conveyance_voucher cv ON v.VOUCHER_ID = cv.VOUCHER_ID\r\n"
				+ "    WHERE v.DELETE_STATUS IS NULL OR v.DELETE_STATUS = 'N'\r\n" + ") cons \r\n"
				+ "WHERE cons.TRIP_ID = ?\r\n" + "GROUP BY cons.TRIP_ID, cons.category, cons.VOUCHER_DATE \r\n"
				+ "ORDER BY cons.VOUCHER_DATE DESC;\r\n" + "";

//		System.out.println(query);
		try (
				// Establish a connection
				Connection connection = DriverManager.getConnection(url, user, password);

				// Create a prepared statement
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			// Set the parameter value
			preparedStatement.setInt(1, tripId);
//			preparedStatement.setInt(2, tripId);
//			preparedStatement.setInt(3, tripId);

			// Execute the query and obtain a result set
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				// Iterate through the result set
				while (resultSet.next()) {
					consolidatedBreakageSummaryDto = new ConsolidatedBreakageSummaryDto();
					consolidatedBreakageSummaryDto.setTripID(resultSet.getInt("TRIP_ID"));
					consolidatedBreakageSummaryDto.setCategory(resultSet.getString("category"));
					consolidatedBreakageSummaryDto.setConsolidatedDate(resultSet.getString("VOUCHER_DATE"));
					consolidatedBreakageSummaryDto.setAmount(resultSet.getBigDecimal("AMOUNT"));
					consolidatedBreakageSummaryDto.setClaimedAmount(resultSet.getBigDecimal("AMOUNT"));
					consolidatedBreakageSummaryDto.setVoucherID(resultSet.getString("VOUCHER_IDS"));

					consBreakageSummaryList.add(consolidatedBreakageSummaryDto);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

//		check if the trip corresponding data is exists in data-base (table: Consolidation-Summary) then fetch the data from the existing table and delete it then save the new Updated data in database 
		Boolean isRecordNotExists = ivcsRepository.findByTrip(tripRepository.findById(tripId).get()).isEmpty();

		if (isRecordNotExists) {

//			First Save In DataBase ConsolidatedBreakageSummary 

			List<ConsolidatedBreakageSummary> listOfConsolidatedSummary = saveConsolicdatedDataAtTimeOfCalculation(
					consBreakageSummaryList);

//			First Save the child table detail also
			for (ConsolidatedBreakageSummary consolidatedBreakageSummary : listOfConsolidatedSummary) {
				consolidatedExpenseDetail = new ConsolidatedExpenseDetail();
				consolidatedExpenseDetail.setConsolidatedBreakageSummary(consolidatedBreakageSummary);
				consolidatedExpenseDetail.setVoucherIds(consBreakageSummaryList.get(count).getVoucherID());

				listOfConsolidatedDetails.add(consolidatedExpenseDetail);
				count++;
			}

			IntStream.range(0, Math.min(consBreakageSummaryList.size(), listOfConsolidatedSummary.size()))
					.forEach(index -> {
						ConsolidatedBreakageSummary element = listOfConsolidatedSummary.get(index);
						ConsolidatedBreakageSummaryDto dtoElement = consBreakageSummaryList.get(index);
						dtoElement.setTripConsolidationId(element.getTripConsolidationID());
					});
		} else {
			ivcsRepository.deleteByTrip(tripRepository.findById(tripId).get());

//			This is the new updated data save

			List<ConsolidatedBreakageSummary> listOfConsolidatedSummary = saveConsolicdatedDataAtTimeOfCalculation(
					consBreakageSummaryList);
			IntStream.range(0, Math.min(consBreakageSummaryList.size(), listOfConsolidatedSummary.size()))
					.forEach(index -> {
						ConsolidatedBreakageSummary element = listOfConsolidatedSummary.get(index);
						ConsolidatedBreakageSummaryDto dtoElement = consBreakageSummaryList.get(index);
						dtoElement.setTripConsolidationId(element.getTripConsolidationID());
					});

			for (ConsolidatedBreakageSummary consolidatedBreakageSummary : listOfConsolidatedSummary) {
				consolidatedExpenseDetail = new ConsolidatedExpenseDetail();
				consolidatedExpenseDetail.setConsolidatedBreakageSummary(consolidatedBreakageSummary);
				consolidatedExpenseDetail.setVoucherIds(consBreakageSummaryList.get(count).getVoucherID());

				listOfConsolidatedDetails.add(consolidatedExpenseDetail);
				count++;
			}

		}
		consolidatedExpenseDetailRepository.saveAll(listOfConsolidatedDetails);

		return consBreakageSummaryList;
	}

	private List<ConsolidatedBreakageSummary> saveConsolicdatedDataAtTimeOfCalculation(
			List<ConsolidatedBreakageSummaryDto> consolidatedBreakageSummaryDtoList) {
		ConsolidatedBreakageSummary consolidatedBreakageSummary = null;
		List<ConsolidatedBreakageSummary> consolidatedBreakageSummariesList = new ArrayList<ConsolidatedBreakageSummary>();

		for (ConsolidatedBreakageSummaryDto consolidatedBreakage : consolidatedBreakageSummaryDtoList) {
			consolidatedBreakageSummary = new ConsolidatedBreakageSummary();
			consolidatedBreakageSummary.setAmount(consolidatedBreakage.getAmount());
			consolidatedBreakageSummary.setClaimedAmount(consolidatedBreakage.getClaimedAmount());
			consolidatedBreakageSummary.setCategory(consolidatedBreakage.getCategory());
			consolidatedBreakageSummary.setConsolidationDate(consolidatedBreakage.getConsolidatedDate());
			consolidatedBreakageSummary.setTrip(getTripByTripId(consolidatedBreakage.getTripID()));
			consolidatedBreakageSummary.setExceptionReason(consolidatedBreakage.getExceptionReason());

			consolidatedBreakageSummariesList.add(consolidatedBreakageSummary);
		}
		return ivcsRepository.saveAll(consolidatedBreakageSummariesList);
	}

	public List<ConsolidatedBreakageSummary> saveConsolicdatedData(
			List<ConsolidatedBreakageSummaryDto> consolidatedBreakageSummaryDtoData) {
		ConsolidatedBreakageSummary consolidatedBreakageSummary = null;
		List<ConsolidatedBreakageSummary> consolidatedBreakageSummariesList = new ArrayList<ConsolidatedBreakageSummary>();

		for (ConsolidatedBreakageSummaryDto consolidatedBreakage : consolidatedBreakageSummaryDtoData) {
//			Changes Are Made Here For Update The Data View
			if (consolidatedBreakage.getTripConsolidationId() == null) {
				consolidatedBreakageSummary = new ConsolidatedBreakageSummary();
				consolidatedBreakageSummary.setAmount(consolidatedBreakage.getAmount());
				consolidatedBreakageSummary.setClaimedAmount(consolidatedBreakage.getClaimedAmount());
				consolidatedBreakageSummary.setCategory(consolidatedBreakage.getCategory());
				consolidatedBreakageSummary.setConsolidationDate(consolidatedBreakage.getConsolidatedDate());
				consolidatedBreakageSummary.setTrip(getTripByTripId(consolidatedBreakage.getTripID()));
				consolidatedBreakageSummary.setExceptionReason(consolidatedBreakage.getExceptionReason());

				consolidatedBreakageSummariesList.add(consolidatedBreakageSummary);
			} else {
				ConsolidatedBreakageSummary consolidatedBreakageSummaryEntity = ivcsRepository
						.findById(consolidatedBreakage.getTripConsolidationId()).get();
				consolidatedBreakageSummaryEntity.setTripConsolidationID(consolidatedBreakage.getTripConsolidationId());
				consolidatedBreakageSummaryEntity.setAmount(consolidatedBreakage.getAmount());
				consolidatedBreakageSummaryEntity.setClaimedAmount(consolidatedBreakage.getClaimedAmount());
				consolidatedBreakageSummaryEntity.setCategory(consolidatedBreakage.getCategory());
				consolidatedBreakageSummaryEntity.setConsolidationDate(consolidatedBreakage.getConsolidatedDate());
				consolidatedBreakageSummaryEntity.setTrip(getTripByTripId(consolidatedBreakage.getTripID()));
				consolidatedBreakageSummaryEntity.setExceptionReason(consolidatedBreakage.getExceptionReason());

				consolidatedBreakageSummariesList.add(consolidatedBreakageSummaryEntity);
			}

		}

		Integer tripID = (consolidatedBreakageSummariesList == null || consolidatedBreakageSummariesList.isEmpty()) ? 0
				: consolidatedBreakageSummariesList.stream().findFirst().map((x) -> x.getTrip().getTripID()).get();

		Trip trip = getTripByTripId(tripID);
		trip.setExpenseStatus("S");
		tripRepository.save(trip);

		// Create first instance of WorkflowTask
//		Boolean approvalRequired = Boolean.TRUE;
//		if (approvalRequiredWhenRulePassed.equalsIgnoreCase("N")) {
//			approvalRequired = Boolean.FALSE;
//			for (ConsolidatedBreakageSummaryDto consolidatedBreakage : consolidatedBreakageSummaryDtoData) {
//				if (consolidatedBreakage.getClaimedAmount().compareTo(consolidatedBreakage.getAmount()) > 0) {
//					approvalRequired = Boolean.TRUE;
//					break;
//				}
//			}
//		}
//		if (Boolean.TRUE.equals(approvalRequired)) {
//			ReferenceDto reference = new ReferenceDto();
//			reference.setReferenceId(trip.getTripID());
//			reference.setReferenceType(TRIP);
//			workflowServiceImpl.processFirstWorkflowTask(reference);
//		}
		Boolean isFinalSubmit = consolidatedBreakageSummaryDtoData.stream()
				.allMatch(consolidatedEntity -> consolidatedEntity.getIsFinalSubmit().equalsIgnoreCase("Y"));
		if (isFinalSubmit) {
			ReferenceDto reference = new ReferenceDto();
			reference.setReferenceId(trip.getTripID());
			reference.setReferenceType(TRIP);
			workflowServiceImpl.processFirstWorkflowTask(reference);
		}
		return ivcsRepository.saveAll(consolidatedBreakageSummariesList);
	}

	public List<HistoryOfConsolidatedData> getAllHistory(Integer employeeId) {

		List<HistoryOfConsolidatedData> listOfHistoryOfConsolidatedData = new ArrayList<HistoryOfConsolidatedData>();
		HistoryOfConsolidatedData historyOfConsolidatedData = null;

		String query = "select *\r\n" + "from\r\n" + "(\r\n"
				+ "select t.TRIP_ID as TRIP_ID, t.FROM_DATE as FROM_DATE, t.TO_DATE as TO_DATE, t.TRIP_NAME as TRIP_NAME, sum(csv.CLAIMED_AMOUNT) as AMOUNT,\r\n"
				+ "t.EXPENSE_STATUS as EXPENSE_STATUS\r\n" + "from trip t, consolidatedbreakagesummary csv\r\n"
				+ "where t.TRIP_ID = csv.TRIP_ID\r\n" + "and t.EXPENSE_STATUS != \"D\"\r\n"
				+ "and t.EMPLOYEE_ID = ?\r\n" + "and t.DELETE_STATUS = \"N\"" + "group by t.TRIP_ID\r\n"
				+ "order by csv.TRIP_ID desc, t.FROM_DATE desc\r\n" + ") cons;";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, employeeId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				while (resultSet.next()) {
					historyOfConsolidatedData = new HistoryOfConsolidatedData();
					historyOfConsolidatedData.setTripID(resultSet.getString("TRIP_ID"));
					historyOfConsolidatedData
							.setFromDate(new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("FROM_DATE")));
					historyOfConsolidatedData
							.setToDate(new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("TO_DATE")));
					historyOfConsolidatedData.setAmount(resultSet.getString("AMOUNT"));
					historyOfConsolidatedData.setTripName(resultSet.getString("TRIP_NAME"));
					historyOfConsolidatedData.setExpenseStatus(resultSet.getString("EXPENSE_STATUS"));

					listOfHistoryOfConsolidatedData.add(historyOfConsolidatedData);

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listOfHistoryOfConsolidatedData;
	}

	public List<HistoryOfConsolidatedData> getAllHistoryOfDraftTrip(Integer employeeId) {

		List<HistoryOfConsolidatedData> listOfHistoryOfConsolidatedData = new ArrayList<HistoryOfConsolidatedData>();
		HistoryOfConsolidatedData historyOfConsolidatedData = null;

		String query = "select * from (select t.TRIP_ID as TRIP_ID, t.FROM_DATE as FROM_DATE, t.TO_DATE as TO_DATE, \r\n"
				+ "t.TRIP_NAME as TRIP_NAME, t.EXPENSE_STATUS as EXPENSE_STATUS, \r\n"
				+ "(select group_concat(p.project_code) from project p, trip_projects tp where tp.TRIP_ID = t.TRIP_ID AND tp.PROJECT_ID = p.project_id) as PROJECT_CODE, \r\n"
				+ "(select group_concat(cc.cost_code) from cost_code cc, trip_costs tc where tc.TRIP_ID = t.TRIP_ID AND tc.COST_ID = cc.cost_id) as COST_CODE\r\n"
				+ "from trip t\r\n"
				+ "where t.EXPENSE_STATUS = \"D\" and t.EMPLOYEE_ID = ? and t.DELETE_STATUS = \"N\"\r\n"
				+ "group by t.TRIP_ID order by t.TRIP_ID desc, t.FROM_DATE desc) cons";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, employeeId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				while (resultSet.next()) {
					historyOfConsolidatedData = new HistoryOfConsolidatedData();
					historyOfConsolidatedData.setTripID(resultSet.getString("TRIP_ID"));
					historyOfConsolidatedData
							.setFromDate(new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("FROM_DATE")));
					historyOfConsolidatedData
							.setToDate(new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("TO_DATE")));
					historyOfConsolidatedData.setTripName(resultSet.getString("TRIP_NAME"));
					historyOfConsolidatedData
							.setProjectCodeList(!StringUtils.isBlank(resultSet.getString("PROJECT_CODE"))
									? Arrays.asList(resultSet.getString("PROJECT_CODE").split(","))
									: new ArrayList<String>());
					historyOfConsolidatedData.setCostCodeList(!StringUtils.isBlank(resultSet.getString("COST_CODE"))
							? Arrays.asList(resultSet.getString("COST_CODE").split(","))
							: new ArrayList<String>());

					listOfHistoryOfConsolidatedData.add(historyOfConsolidatedData);

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listOfHistoryOfConsolidatedData;
	}

	public List<HistoryOfConsolidatedData> getAllHistoryOfUpcomingTrip(Integer employeeId) {

		List<HistoryOfConsolidatedData> listOfHistoryOfConsolidatedData = new ArrayList<HistoryOfConsolidatedData>();
		HistoryOfConsolidatedData historyOfConsolidatedData = null;

		String query = "select * from (select t.TRIP_ID as TRIP_ID, t.FROM_DATE as FROM_DATE, t.TO_DATE as TO_DATE, t.TRIP_NAME as TRIP_NAME, t.EXPENSE_STATUS as EXPENSE_STATUS\r\n"
				+ "from trip t\r\n"
				+ "where t.EXPENSE_STATUS = \"T\" and t.EMPLOYEE_ID = ? and t.DELETE_STATUS = \"N\"\r\n"
				+ "group by t.TRIP_ID order by t.TRIP_ID desc, t.FROM_DATE desc) cons;";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, employeeId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				while (resultSet.next()) {
					historyOfConsolidatedData = new HistoryOfConsolidatedData();
					historyOfConsolidatedData.setTripID(resultSet.getString("TRIP_ID"));
					historyOfConsolidatedData
							.setFromDate(new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("FROM_DATE")));
					historyOfConsolidatedData
							.setToDate(new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("TO_DATE")));
					historyOfConsolidatedData.setTripName(resultSet.getString("TRIP_NAME"));

					listOfHistoryOfConsolidatedData.add(historyOfConsolidatedData);

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listOfHistoryOfConsolidatedData;
	}

	public List<SingleVoucherDto> singleExpenseForFood(String isSingleExpense) {

//		This Query is Now only Total amount send GST amount not send with them
		String query = "select 'food' as category, v.VOUCHER_ID as VOUCHER_ID, v.VOUCHER_DATE as VOUCHER_DATE, fv.TOTAL_AMOUNT as AMOUNT\r\n"
				+ "from voucher v, food_voucher fv\r\n" + "where v.VOUCHER_ID = fv.VOUCHER_ID\r\n"
				+ "and v.SINGLE_EXPENSE = \"Y\"\r\n" + "order by VOUCHER_ID desc\r\n" + "limit 1;";

		SingleVoucherDto singleVoucherDto = null;
		List<SingleVoucherDto> listOfSingleVoucherDtos = new ArrayList<SingleVoucherDto>();
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					singleVoucherDto = new SingleVoucherDto();
					singleVoucherDto.setAmount(resultSet.getString("AMOUNT"));
					singleVoucherDto.setVoucherID(resultSet.getString("VOUCHER_ID"));
					singleVoucherDto.setVoucherDate(resultSet.getString("VOUCHER_DATE"));
					singleVoucherDto.setBillType(resultSet.getString("category"));
				}
			} catch (SQLException e) {
				// TODO: handle exception
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}
		listOfSingleVoucherDtos.add(singleVoucherDto);
		return listOfSingleVoucherDtos;
	}

	public List<SingleVoucherDto> singleExpenseForConveyance(String isSingleExpense) {

//		This Query is Now only Total amount send GST amount not send with them
		String query = "select 'conveyance' as category,v.VOUCHER_ID as VOUCHER_ID, v.VOUCHER_DATE as VOUCHER_DATE, v.TOTAL_AMOUNT as AMOUNT\r\n"
				+ "from voucher v, conveyance_voucher cv\r\n" + "where v.VOUCHER_ID = cv.VOUCHER_ID\r\n"
				+ "and v.SINGLE_EXPENSE = \"Y\"\r\n" + "order by VOUCHER_ID DESC\r\n" + "limit 1";

		SingleVoucherDto singleVoucherDto = null;
		List<SingleVoucherDto> listOfSingleVoucherDtos = new ArrayList<SingleVoucherDto>();
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					singleVoucherDto = new SingleVoucherDto();
					singleVoucherDto.setAmount(resultSet.getString("AMOUNT"));
					singleVoucherDto.setVoucherID(resultSet.getString("VOUCHER_ID"));
					singleVoucherDto.setVoucherDate(resultSet.getString("VOUCHER_DATE"));
					singleVoucherDto.setBillType(resultSet.getString("category"));
				}
			} catch (SQLException e) {
				// TODO: handle exception
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}
		listOfSingleVoucherDtos.add(singleVoucherDto);
		return listOfSingleVoucherDtos;
	}

//	Submitted trip details with Trip_Name 
//	public List<HistoryOfConsolidatedData> getAllHistoryOfSubmittedData(String ) {
//		
//	}

	private Trip getTripByTripId(Integer tripId) {
		Optional<Trip> tripEntity = tripRepository.findById(tripId);
		return tripEntity.get();
	}

//	private static Date parseStringToDate(String date) {
//		Date outDate = null;
//		try {
//			outDate = new java.sql.Date(((java.util.Date) new SimpleDateFormat("dd-MM-yyyy").parse(date)).getTime());
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		return outDate;
//	}

	public List<ConsolidatedBreakageSummaryDto> consolidatedSummary(Integer tripId) {

		List<ConsolidatedBreakageSummaryDto> consBreakageSummaryList = new ArrayList<ConsolidatedBreakageSummaryDto>();
		ConsolidatedBreakageSummaryDto consolidatedBreakageSummaryDto = null;

		String query = "select cs.TRIP_CONSOLIDATION_ID as TRIP_CONSOLIDATION_ID, cs.CATEGORY as CATEGORY, cs.AMOUNT as AMOUNT, cs.CLAIMED_AMOUNT as CLAIMED_AMOUNT, cs.CONSOLIDATION_DATE as CONSOLIDATION_DATE, cs.TRIP_ID from\r\n"
				+ "consolidatedbreakagesummary cs\r\n" + "where cs.TRIP_ID = ?";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setInt(1, tripId);

			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				while (resultSet.next()) {
					consolidatedBreakageSummaryDto = new ConsolidatedBreakageSummaryDto();
					consolidatedBreakageSummaryDto.setTripConsolidationId(resultSet.getInt("TRIP_CONSOLIDATION_ID"));
					consolidatedBreakageSummaryDto.setTripID(resultSet.getInt("TRIP_ID"));
					consolidatedBreakageSummaryDto.setCategory(resultSet.getString("category"));
					consolidatedBreakageSummaryDto.setConsolidatedDate(resultSet.getString("CONSOLIDATION_DATE"));
					consolidatedBreakageSummaryDto.setAmount(resultSet.getBigDecimal("AMOUNT"));
					consolidatedBreakageSummaryDto.setClaimedAmount(resultSet.getBigDecimal("CLAIMED_AMOUNT"));

					consBreakageSummaryList.add(consolidatedBreakageSummaryDto);

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return consBreakageSummaryList;

	}

}
