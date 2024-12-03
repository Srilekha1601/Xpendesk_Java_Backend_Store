package com.invoiceprocessing.invoiceprocessor.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.mapper.ConsolidatedVoucherMapperImpl;
import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.repository.TripRepository;
import com.invoiceprocessing.invoiceprocessor.response.BarChartInfoDto;
import com.invoiceprocessing.invoiceprocessor.response.BarChartReportUserExpenseDto;
import com.invoiceprocessing.invoiceprocessor.response.ConsolidatedBreakageSummaryDto;
import com.invoiceprocessing.invoiceprocessor.response.DateWiseAmountBreakdownDto;
import com.invoiceprocessing.invoiceprocessor.response.EmployeeDto;
import com.invoiceprocessing.invoiceprocessor.response.ExpenseForEmployeeBarChartReportDto;
import com.invoiceprocessing.invoiceprocessor.response.ExpenseReportDataForBarChartDto;
import com.invoiceprocessing.invoiceprocessor.response.MonthWiseExpenseDto;
import com.invoiceprocessing.invoiceprocessor.response.MonthWiseReportSummaryDto;
import com.invoiceprocessing.invoiceprocessor.response.ReportDto;
import com.invoiceprocessing.invoiceprocessor.response.ReviewerReportForDonutChartDto;
import com.invoiceprocessing.invoiceprocessor.response.SubmittedDataInformation;
import com.invoiceprocessing.invoiceprocessor.response.YearWiseReport;
import com.invoiceprocessing.invoiceprocessor.response.YearlyRepot;
import com.invoiceprocessing.invoiceprocessor.response.YearlyTotalExpenses;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.ReportService;
import com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants;

@Service
public class ReportServiceImpl implements ReportService {

	@Value("${xpendesk.db.url}")
	private String url;

	@Value("${xpendesk.db.user}")
	private String user;

	@Value("${xpendesk.db.password}")
	private String password;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	TripRepository tripRepository;

	@Override
	public ReportDto getAllReportByTripID(Integer tripID) {
		ConsolidatedVoucherMapperImpl consolidationInfo = (ConsolidatedVoucherMapperImpl) applicationContext
				.getBean(XpendeskConstants.CONSOLIDATION_TYPE);

		ReportDto reportDto = new ReportDto();

		Trip trip = tripRepository.findById(tripID).get();

//		Insert data into the trip section of the ReportDto
		reportDto.setFromDate(new SimpleDateFormat("dd-MM-yyyy").format(trip.getFromDate()));
		reportDto.setToDate(new SimpleDateFormat("dd-MM-yyyy").format(trip.getToDate()));
		reportDto.setTripID(trip.getTripID().toString());
		reportDto.setTripName(trip.getTripName());

		List<ConsolidatedBreakageSummaryDto> listOfBreakageDto = consolidationInfo.consolidatedSummary(tripID);
		List<DateWiseAmountBreakdownDto> listOfDateWiseBreakage = new ArrayList<DateWiseAmountBreakdownDto>();
		Map<String, List<ConsolidatedBreakageSummaryDto>> mapper = listOfBreakageDto.stream()
				.collect(Collectors.groupingBy(ConsolidatedBreakageSummaryDto::getConsolidatedDate));

		mapper.forEach((date, consololidatedData) -> {
			DateWiseAmountBreakdownDto dateWiseAmountBreakdownDto = new DateWiseAmountBreakdownDto();
			dateWiseAmountBreakdownDto.setDate(date);
			dateWiseAmountBreakdownDto.setTripID(trip.getTripID().toString());
			for (ConsolidatedBreakageSummaryDto consolidation : consololidatedData) {
				if (StringUtils.equalsAnyIgnoreCase(consolidation.getCategory(), XpendeskConstants.FOOD_TYPE))
					dateWiseAmountBreakdownDto.setFoodAmount(consolidation.getClaimedAmount().toString());
				if (StringUtils.equalsAnyIgnoreCase(consolidation.getCategory(), XpendeskConstants.CONVEYANCE_TYPE))
					dateWiseAmountBreakdownDto.setConveyanceAmount(consolidation.getClaimedAmount().toString());
				if (StringUtils.equalsAnyIgnoreCase(consolidation.getCategory(), XpendeskConstants.ACCOMMODATION_TYPE))
					dateWiseAmountBreakdownDto.setAccommodationAmount(consolidation.getClaimedAmount().toString());
				if (StringUtils.equalsAnyIgnoreCase(consolidation.getCategory(), XpendeskConstants.OUTHER_TYPE))
					dateWiseAmountBreakdownDto.setOuthersAmount(consolidation.getClaimedAmount().toString());
			}
			listOfDateWiseBreakage.add(dateWiseAmountBreakdownDto);
		});
		BigDecimal totalAmount = listOfDateWiseBreakage.stream().map(extractDataAmountElements -> {
			BigDecimal accommodationAmount = new BigDecimal(
					extractDataAmountElements.getAccommodationAmount() == null ? "0.0"
							: extractDataAmountElements.getAccommodationAmount());
			BigDecimal conveyanceAmount = new BigDecimal(extractDataAmountElements.getConveyanceAmount() == null ? "0.0"
					: extractDataAmountElements.getConveyanceAmount());
			BigDecimal foodAmount = new BigDecimal(extractDataAmountElements.getFoodAmount() == null ? "0.0"
					: extractDataAmountElements.getFoodAmount());

			return accommodationAmount.add(conveyanceAmount).add(foodAmount);
		}).reduce(BigDecimal.ZERO, BigDecimal::add);
		reportDto.setAmount(totalAmount.toString());
		reportDto.setListOfDateWiseAmountBreakdown(listOfDateWiseBreakage);
		return reportDto;
	}

	@Override
	public SubmittedDataInformation getAllSubmittedData(Integer employeeID) {

		SubmittedDataInformation submittedDataInformation = null;
		String query = "select EMPLOYEE_ID, INVOICE_TYPE, sum(INVOICE_TYPE_COUNT) as INVOICE_TYPE_COUNT\r\n"
				+ "from\r\n" + "(\r\n"
				+ "select v.EMPLOYEE_ID, v.INVOICE_TYPE, count(v.INVOICE_TYPE) as INVOICE_TYPE_COUNT\r\n"
				+ "from employee e, voucher v\r\n" + "where v.EMPLOYEE_ID = ?\r\n"
				+ "and v.EMPLOYEE_ID = e.EMPLOYEE_ID\r\n" + "and v.INVOICE_TYPE = 'food'\r\n"
				+ "group by v.EMPLOYEE_ID, v.INVOICE_TYPE\r\n" + "\r\n" + "union all\r\n"
				+ "select v.EMPLOYEE_ID, v.INVOICE_TYPE, count(v.INVOICE_TYPE) as INVOICE_TYPE_COUNT\r\n"
				+ "from employee e, voucher v\r\n" + "where v.EMPLOYEE_ID = ?\r\n"
				+ "and v.EMPLOYEE_ID = e.EMPLOYEE_ID\r\n" + "and v.INVOICE_TYPE = 'hotel'\r\n"
				+ "group by v.EMPLOYEE_ID, v.INVOICE_TYPE\r\n" + "\r\n" + "union all\r\n"
				+ "select v.EMPLOYEE_ID, v.INVOICE_TYPE, count(v.INVOICE_TYPE) as INVOICE_TYPE_COUNT\r\n"
				+ "from employee e, voucher v\r\n" + "where v.EMPLOYEE_ID = ?\r\n"
				+ "and v.EMPLOYEE_ID = e.EMPLOYEE_ID\r\n" + "and v.INVOICE_TYPE = 'conveyance'\r\n"
				+ "group by v.EMPLOYEE_ID, v.INVOICE_TYPE\r\n" + ") report\r\n"
				+ "group by report.EMPLOYEE_ID, report.INVOICE_TYPE;";

		try (
				// Establish a connection
				Connection connection = DriverManager.getConnection(url, user, password);

				// Create a prepared statement
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			// Set the parameter value
			preparedStatement.setInt(1, employeeID);
			preparedStatement.setInt(2, employeeID);
			preparedStatement.setInt(3, employeeID);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				submittedDataInformation = new SubmittedDataInformation();
				// Iterate through the result set
				while (resultSet.next()) {
					submittedDataInformation.setEmployeeID(resultSet.getInt("EMPLOYEE_ID"));
					if (StringUtils.equalsAnyIgnoreCase(resultSet.getString("INVOICE_TYPE"), "food")) {
						submittedDataInformation.setTotalFoodInvoices(resultSet.getInt("INVOICE_TYPE_COUNT"));
					} else if (StringUtils.equalsAnyIgnoreCase(resultSet.getString("INVOICE_TYPE"), "conveyance")) {
						submittedDataInformation.setTotalConveyanceInvoices(resultSet.getInt("INVOICE_TYPE_COUNT"));
					} else {
						submittedDataInformation.setTotalHotelInvoices(resultSet.getInt("INVOICE_TYPE_COUNT"));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return submittedDataInformation;
	}

	@Override
	public MonthWiseExpenseDto monthlyDataSummary(Integer employeeID, Integer year) {

		MonthWiseExpenseDto monthWiseExpenseDto = new MonthWiseExpenseDto();
		MonthWiseReportSummaryDto monthWiseReportSummaryDto = null;
		List<MonthWiseReportSummaryDto> listOfMonthlyReportSummaryDto = new ArrayList<MonthWiseReportSummaryDto>();

		String query = "SELECT monthname(STR_TO_DATE(concat(\"2030,\",a.MNTH,\",1\"), \"%Y,%m,%d\")) as MNTH, a.INVOICE_TYPE, a.AMOUNT\r\n"
				+ "from (SELECT MONTH(v.VOUCHER_DATE) AS MNTH,\r\n"
				+ "v.INVOICE_TYPE AS INVOICE_TYPE, SUM(v.CLAIMED_AMOUNT) AS AMOUNT FROM\r\n"
				+ "voucher v WHERE v.VOUCHER_DATE BETWEEN CONCAT(?, '-01-01') AND CONCAT(?, '-12-31')\r\n"
				+ "and EMPLOYEE_ID = ? GROUP BY v.INVOICE_TYPE,\r\n"
				+ "MONTH(v.VOUCHER_DATE) order by  month(v.VOUCHER_DATE)) a";

		try (
				// Establish a connection
				Connection connection = DriverManager.getConnection(url, user, password);

				// Create a prepared statement
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			// Set the parameter value
			preparedStatement.setInt(1, year);
			preparedStatement.setInt(2, year);
			preparedStatement.setInt(3, employeeID);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				MonthWiseReportSummaryDto monthWiseDtoCopy = new MonthWiseReportSummaryDto();
				while (resultSet.next()) {
					monthWiseReportSummaryDto = new MonthWiseReportSummaryDto();
					monthWiseReportSummaryDto.setMonth(resultSet.getString("MNTH"));
					if (monthWiseDtoCopy.getMonth() != null
							&& monthWiseDtoCopy.getMonth().equalsIgnoreCase(resultSet.getString("MNTH"))) {
						if (StringUtils.equalsAnyIgnoreCase(resultSet.getString("INVOICE_TYPE"), "food"))
							monthWiseDtoCopy.setFoodExpense(resultSet.getInt("AMOUNT"));
						if (StringUtils.equalsAnyIgnoreCase(resultSet.getString("INVOICE_TYPE"), "hotel"))
							monthWiseDtoCopy.setHotelExpense(resultSet.getInt("AMOUNT"));
						if (StringUtils.equalsAnyIgnoreCase(resultSet.getString("INVOICE_TYPE"), "accommodation")
								&& (monthWiseDtoCopy.getHotelExpense() != null))
							monthWiseDtoCopy
									.setHotelExpense(monthWiseDtoCopy.getHotelExpense() + resultSet.getInt("AMOUNT"));
						if (StringUtils.equalsAnyIgnoreCase(resultSet.getString("INVOICE_TYPE"), "conveyance"))
							monthWiseDtoCopy.setConveyanceExpense(resultSet.getInt("AMOUNT"));
						monthWiseReportSummaryDto = monthWiseDtoCopy;
						if (!listOfMonthlyReportSummaryDto.isEmpty()) {
							listOfMonthlyReportSummaryDto
									.remove(listOfMonthlyReportSummaryDto.indexOf(monthWiseDtoCopy));
							listOfMonthlyReportSummaryDto.add(monthWiseReportSummaryDto);
						}
					} else {
						monthWiseReportSummaryDto.setMonth(resultSet.getString("MNTH"));
						if (StringUtils.equalsAnyIgnoreCase(resultSet.getString("INVOICE_TYPE"), "food"))
							monthWiseReportSummaryDto.setFoodExpense(resultSet.getInt("AMOUNT"));
						if (StringUtils.equalsAnyIgnoreCase(resultSet.getString("INVOICE_TYPE"), "hotel"))
							monthWiseReportSummaryDto.setHotelExpense(resultSet.getInt("AMOUNT"));
						if (StringUtils.equalsAnyIgnoreCase(resultSet.getString("INVOICE_TYPE"), "accommodation")
								&& (monthWiseReportSummaryDto.getHotelExpense() != null))
							monthWiseReportSummaryDto.setHotelExpense(
									monthWiseReportSummaryDto.getHotelExpense() + resultSet.getInt("AMOUNT"));
						if (StringUtils.equalsAnyIgnoreCase(resultSet.getString("INVOICE_TYPE"), "conveyance"))
							monthWiseReportSummaryDto.setConveyanceExpense(resultSet.getInt("AMOUNT"));
						monthWiseDtoCopy = monthWiseReportSummaryDto;
						listOfMonthlyReportSummaryDto.add(monthWiseDtoCopy);
					}

				}
				monthWiseExpenseDto.setEmployeeID(employeeID);

				List<String> listOfMonth = List.of("January", "February", "March", "April", "May", "June", "July",
						"August", "September", "October", "November", "December");

				listOfMonthlyReportSummaryDto
						.sort(Comparator.comparingInt(monthDto -> listOfMonth.indexOf(monthDto.getMonth())));

				List<String> missingIds = listOfMonth.stream().filter(month -> listOfMonthlyReportSummaryDto.stream()
						.noneMatch(obj -> obj.getMonth().equalsIgnoreCase(month))).collect(Collectors.toList());

				List<MonthWiseReportSummaryDto> missingObjects = missingIds.stream()
						.map(month -> new MonthWiseReportSummaryDto(month, 0, 0, 0)).collect(Collectors.toList());
				if (!missingIds.isEmpty())
					listOfMonthlyReportSummaryDto.addAll(missingObjects);
				monthWiseExpenseDto.setListOfMonthWiseReportDto(listOfMonthlyReportSummaryDto);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return monthWiseExpenseDto;
	}

	@Override
	public YearWiseReport reportGenerationInMonthAndYear(Integer employeeID, Integer year) {

		String query = "SELECT\r\n" + "month_table.MNTH,\r\n" + "COALESCE(SUM(v.CLAIMED_AMOUNT), 0) AS AMOUNT\r\n"
				+ "FROM\r\n" + "(\r\n" + "SELECT 'January' AS MNTH, 1 AS MN\r\n" + "UNION ALL SELECT 'February', 2\r\n"
				+ "UNION ALL SELECT 'March', 3\r\n" + "UNION ALL SELECT 'April', 4\r\n"
				+ "UNION ALL SELECT 'May', 5\r\n" + "UNION ALL SELECT 'June', 6\r\n" + "UNION ALL SELECT 'July', 7\r\n"
				+ "UNION ALL SELECT 'August', 8\r\n" + "UNION ALL SELECT 'September', 9\r\n"
				+ "UNION ALL SELECT 'October', 10\r\n" + "UNION ALL SELECT 'November', 11\r\n"
				+ "UNION ALL SELECT 'December', 12\r\n" + ") AS month_table\r\n" + "LEFT JOIN\r\n" + "voucher v\r\n"
				+ "ON\r\n" + "month_table.MN = MONTH(v.INVOICE_DATE)\r\n"
				+ "AND v.VOUCHER_DATE BETWEEN CONCAT(?, '-01-01') AND CONCAT(?, '-12-31')\r\n"
				+ "AND v.EMPLOYEE_ID = ?\r\n" + "GROUP BY\r\n" + "month_table.MNTH, month_table.MN\r\n" + "ORDER BY\r\n"
				+ "month_table.MN;";
		YearWiseReport yearWiseReport = new YearWiseReport();
		List<YearlyRepot> yearlyRportList = new ArrayList<YearlyRepot>();
		YearlyRepot yearlyReport = null;
		try (
				// Establish a connection
				Connection connection = DriverManager.getConnection(url, user, password);

				// Create a prepared statement
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			// Set the parameter value
			preparedStatement.setInt(1, year);
			preparedStatement.setInt(2, year);
			preparedStatement.setInt(3, employeeID);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				while (resultSet.next()) {
					yearlyReport = new YearlyRepot();
					yearlyReport.setMonthsName(resultSet.getString("MNTH"));
					yearlyReport.setAmount(resultSet.getBigDecimal("AMOUNT"));
					yearlyRportList.add(yearlyReport);
				}

				yearWiseReport.setEmployeeID(employeeID);
				yearWiseReport.setMonthWiseReport(yearlyRportList);
			} catch (SQLException e) {
				// TODO: handle exception
				throw new SQLException("Something Went Wrong...");
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}

		return yearWiseReport;
	}

	@Override
	public YearlyTotalExpenses yearlyExpenses(Integer employeeID, Integer year) {

		String query = "\r\n" + "WITH years AS (\r\n" + "SELECT ? AS YER\r\n" + ")\r\n" + "SELECT \r\n" + "y.YER, \r\n"
				+ "COALESCE(SUM(v.CLAIMED_AMOUNT), 0) AS AMOUNT\r\n" + "FROM \r\n" + "years y\r\n" + "LEFT JOIN \r\n"
				+ "voucher v ON YEAR(v.VOUCHER_DATE) = y.YER AND \r\n"
				+ "v.VOUCHER_DATE BETWEEN CONCAT(?, '-01-01') AND CONCAT(?, '-12-31') AND \r\n"
				+ "v.EMPLOYEE_ID = ?\r\n" + "GROUP BY \r\n" + "y.YER;";

		YearlyTotalExpenses yearlyTotalExpenses = null;
		try (
				// Establish a connection
				Connection connection = DriverManager.getConnection(url, user, password);

				// Create a prepared statement
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			// Set the parameter value
			preparedStatement.setInt(1, year);
			preparedStatement.setInt(2, year);
			preparedStatement.setInt(3, year);
			preparedStatement.setInt(4, employeeID);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				yearlyTotalExpenses = new YearlyTotalExpenses();
				while (resultSet.next()) {
					yearlyTotalExpenses = new YearlyTotalExpenses();
					yearlyTotalExpenses.setEmployeeID(employeeID.toString());
					yearlyTotalExpenses.setYear(resultSet.getString("YER"));
					yearlyTotalExpenses.setAmount(resultSet.getBigDecimal("AMOUNT"));
				}

			} catch (SQLException e) {
				// TODO: handle exception
				throw new SQLException("Something Went Wrong...");
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}
		return yearlyTotalExpenses;
	}

//	.................. Report Services For Reviewer Section ..................

	@Override
	public ReviewerReportForDonutChartDto getDonutChartReportForReviewer(EmployeeDto employee) {
		// TODO Auto-generated method stub

		String query = "SELECT\r\n"
				+ "COUNT(CASE WHEN wt.action_type = \"S\" THEN wt.action_type END) AS PENDING_TASK,\r\n"
				+ "COUNT(CASE WHEN wt.action_type = \"A\" THEN wt.action_type END) AS APPROVED_TASK,\r\n"
				+ "COUNT(CASE WHEN wt.action_type = \"R\" THEN wt.action_type END) AS REJECTED_TASK\r\n" + "FROM\r\n"
				+ "workflow_task wt\r\n" + "WHERE\r\n" + "wt.assigned_to = ? and wt.step_id is not null;";

		ReviewerReportForDonutChartDto reviewerReportForDonutChartDto = null;

		try (
				// Establish a connection
				Connection connection = DriverManager.getConnection(url, user, password);

				// Create a prepared statement
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			// Set the parameter value
			preparedStatement.setInt(1, employee.getEmployeeId());

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				reviewerReportForDonutChartDto = new ReviewerReportForDonutChartDto();
				while (resultSet.next()) {
					reviewerReportForDonutChartDto.setPendingTask(resultSet.getInt("PENDING_TASK"));
					reviewerReportForDonutChartDto.setApprovedTask(resultSet.getInt("APPROVED_TASK"));
					reviewerReportForDonutChartDto.setRejectedTask(resultSet.getInt("REJECTED_TASK"));
				}

			} catch (SQLException e) {
				// TODO: handle exception
				throw new SQLException("Something Went Wrong...");
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}

		return reviewerReportForDonutChartDto;
	}

	@Override
	public List<BarChartReportUserExpenseDto> getBarChartReportForUserExpense(
			BarChartReportUserExpenseDto barChartReportUserExpenseDto) {
		// TODO Auto-generated method stub

		String query = "select e.EMPLOYEE_ID as EMPLOYEE_ID , e.EMPLOYEE_NAME as EMPLOYEE_NAME,\r\n"
				+ "count(CASE WHEN v.INVOICE_TYPE = \"food\" THEN v.INVOICE_TYPE END) as TOTAL_FOOD_INVOICES ,\r\n"
				+ "count(CASE WHEN v.INVOICE_TYPE = \"hotel\" THEN v.INVOICE_TYPE END) as TOTAL_HOTEL_INVOICES ,\r\n"
				+ "count(CASE WHEN v.INVOICE_TYPE = \"conveyance\" THEN v.INVOICE_TYPE END) as TOTAL_CONVEYANCE_INVOICES\r\n"
				+ "from workflow_task wt \r\n" + "inner join employee e on e.EMPLOYEE_ID = wt.generated_by\r\n"
				+ "inner join voucher v on v.VOUCHER_ID = wt.reference_id\r\n" + "where \r\n" + "wt.assigned_to = ?\r\n"
				+ "group by wt.generated_by";

		BarChartReportUserExpenseDto barChartReportDto = null;
		BarChartInfoDto barChartInfoDto = null;
		List<BarChartReportUserExpenseDto> listOfBarChartReportUserExpenseDto = new ArrayList<BarChartReportUserExpenseDto>();

		try (
				// Establish a connection
				Connection connection = DriverManager.getConnection(url, user, password);

				// Create a prepared statement
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			// Set the parameter value
			preparedStatement.setInt(1, barChartReportUserExpenseDto.getEmployeeId());

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				while (resultSet.next()) {
					barChartReportDto = new BarChartReportUserExpenseDto();
					barChartInfoDto = new BarChartInfoDto();
					barChartInfoDto.setTotalFoodInvoices(resultSet.getInt("TOTAL_FOOD_INVOICES"));
					barChartInfoDto.setTotalHotelInvoices(resultSet.getInt("TOTAL_HOTEL_INVOICES"));
					barChartInfoDto.setTotalConveyanceInvoices(resultSet.getInt("TOTAL_CONVEYANCE_INVOICES"));
					barChartReportDto.setEmployeeName(resultSet.getString("EMPLOYEE_NAME"));
					barChartReportDto.setEmployeeId(resultSet.getInt("EMPLOYEE_ID"));
					barChartReportDto.setBarChartInfoDto(barChartInfoDto);

					listOfBarChartReportUserExpenseDto.add(barChartReportDto);
				}

			} catch (SQLException e) {
				// TODO: handle exception
				throw new SQLException("Something Went Wrong...");
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}
		return listOfBarChartReportUserExpenseDto;
	}

	@Override
	public List<ExpenseForEmployeeBarChartReportDto> barChartReportForCheckIndividualUsers(
			ExpenseForEmployeeBarChartReportDto expenseForEmployeeBarChartReportDto) {
		// TODO Auto-generated method stub

		String query = "select e.EMPLOYEE_ID as EMPLOYEE_ID , e.EMPLOYEE_NAME as EMPLOYEE_NAME,\r\n"
				+ "count(CASE WHEN wt.action_type = \"S\" THEN wt.action_type END) as TOTAL_PENDING_INVOICES ,\r\n"
				+ "count(CASE WHEN wt.action_type = \"A\" THEN wt.action_type END) as TOTAL_APPROVED_INVOICES ,\r\n"
				+ "count(CASE WHEN wt.action_type = \"R\" THEN wt.action_type END) as TOTAL_REJECTED_INVOICES \r\n"
				+ "from workflow_task wt \r\n" + "inner join employee e on e.EMPLOYEE_ID = wt.generated_by\r\n"
				+ "inner join voucher v on v.VOUCHER_ID = wt.reference_id\r\n" + "where \r\n" + "wt.assigned_to = ?\r\n"
				+ "group by wt.generated_by; ";

		ExpenseForEmployeeBarChartReportDto expenseForEmployeeBarChartReport = null;
		ExpenseReportDataForBarChartDto expenseReportDataForBarChartDto = null;
		List<ExpenseForEmployeeBarChartReportDto> listOfExpenseForEmployeeBarChartReportDto = new ArrayList<ExpenseForEmployeeBarChartReportDto>();

		try (
				// Establish a connection
				Connection connection = DriverManager.getConnection(url, user, password);

				// Create a prepared statement
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			// Set the parameter value
			preparedStatement.setInt(1, expenseForEmployeeBarChartReportDto.getEmployeeId());

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				while (resultSet.next()) {
					expenseForEmployeeBarChartReport = new ExpenseForEmployeeBarChartReportDto();
					expenseReportDataForBarChartDto = new ExpenseReportDataForBarChartDto();
					expenseReportDataForBarChartDto
							.setTotalApprovedInvoices(resultSet.getInt("TOTAL_APPROVED_INVOICES"));
					expenseReportDataForBarChartDto.setTotalPendingInvoices(resultSet.getInt("TOTAL_PENDING_INVOICES"));
					expenseReportDataForBarChartDto
							.setTotalRejectedInvoices(resultSet.getInt("TOTAL_REJECTED_INVOICES"));
					expenseForEmployeeBarChartReport.setEmployeeId(resultSet.getInt("EMPLOYEE_ID"));
					expenseForEmployeeBarChartReport.setEmployeeName(resultSet.getString("EMPLOYEE_NAME"));
					expenseForEmployeeBarChartReport.setExpenseReportDataForBarChart(expenseReportDataForBarChartDto);

					listOfExpenseForEmployeeBarChartReportDto.add(expenseForEmployeeBarChartReport);
				}

			} catch (SQLException e) {
				// TODO: handle exception
				throw new SQLException("Something Went Wrong...");
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}
		return listOfExpenseForEmployeeBarChartReportDto;
	}

}
