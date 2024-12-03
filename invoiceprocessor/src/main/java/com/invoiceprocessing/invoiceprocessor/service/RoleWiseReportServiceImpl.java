package com.invoiceprocessing.invoiceprocessor.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.response.ExpenseReportDto;
import com.invoiceprocessing.invoiceprocessor.response.RoleWiseReportDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.RoleWiseReportService;
import com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants;

@Service
public class RoleWiseReportServiceImpl implements RoleWiseReportService {

	@Value("${xpendesk.db.url}")
	private String url;

	@Value("${xpendesk.db.user}")
	private String user;

	@Value("${xpendesk.db.password}")
	private String password;

//	@Override
//	public List<RoleWiseReportDto> getReportForAllUsers() throws SQLException {
//		// TODO Auto-generated method stub
//
//		String url = "jdbc:mysql://localhost:3306/expendesk";
//		String user = "root";
//		String password = "#Sroy@1010";
//
//		String query = "SELECT \r\n" + "e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
//				+ "    e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "    g.GRADE AS GRADE,\r\n"
//				+ "    'conveyance' AS INVOICE_TYPE,\r\n" + "    SUM(v.TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n" + "FROM \r\n"
//				+ "    employee e\r\n" + "JOIN \r\n" + "    voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n"
//				+ "JOIN \r\n" + "    grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "WHERE \r\n"
//				+ "    v.INVOICE_TYPE = 'conveyance'\r\n" + "GROUP BY \r\n" + "    e.EMPLOYEE_ID, g.GRADE\r\n" + "\r\n"
//				+ "UNION ALL\r\n" + "SELECT \r\n" + "    EMPLOYEE_NAME,\r\n" + "    EMPLOYEE_CODE,\r\n"
//				+ "    GRADE,\r\n" + "    INVOICE_TYPE,\r\n" + "    SUM(TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n"
//				+ "FROM (\r\n" + "    SELECT \r\n" + "e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
//				+ "        e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "        g.GRADE AS GRADE,\r\n"
//				+ "        'food' AS INVOICE_TYPE,\r\n" + "        SUM(v.TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n"
//				+ "    FROM \r\n" + " employee e\r\n" + "    JOIN \r\n"
//				+ "        voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "    JOIN \r\n"
//				+ "        grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "    WHERE \r\n"
//				+ "        v.INVOICE_TYPE = 'food'\r\n" + "    GROUP BY \r\n"
//				+ "        e.EMPLOYEE_NAME, e.EMPLOYEE_CODE, g.GRADE, 'food'\r\n" + "    UNION ALL\r\n"
//				+ "    SELECT \r\n" + "        e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
//				+ "        e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "        g.GRADE AS GRADE,\r\n"
//				+ "        'food' AS INVOICE_TYPE,\r\n" + "        SUM(fvd.AMOUNT) AS TOTAL_AMOUNT\r\n"
//				+ "    FROM \r\n" + "        employee e\r\n" + "    JOIN \r\n"
//				+ "        voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "    JOIN \r\n"
//				+ "        grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "    JOIN \r\n"
//				+ "        hotel_voucher hv ON hv.VOUCHER_ID = v.VOUCHER_ID\r\n" + "    JOIN \r\n"
//				+ "        food_voucher_detail fvd ON hv.HOTEL_VOUCHER_ID = fvd.HOTEL_VOUCHER_ID\r\n" + "    WHERE \r\n"
//				+ "        v.INVOICE_TYPE = 'hotel'\r\n" + "    GROUP BY \r\n"
//				+ "        e.EMPLOYEE_NAME, e.EMPLOYEE_CODE, g.GRADE, 'food'\r\n" + ") food_temporary\r\n"
//				+ "GROUP BY EMPLOYEE_NAME, EMPLOYEE_CODE, GRADE, INVOICE_TYPE \r\n" + "\r\n" + "UNION ALL\r\n"
//				+ "SELECT \r\n" + "    EMPLOYEE_NAME,\r\n" + "    EMPLOYEE_CODE,\r\n" + "    GRADE,\r\n"
//				+ "    INVOICE_TYPE,\r\n" + "    SUM(TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n" + "FROM (\r\n"
//				+ "    SELECT \r\n" + "        e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
//				+ "        e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "        g.GRADE AS GRADE,\r\n"
//				+ "        'accommodation' AS INVOICE_TYPE,\r\n" + "        SUM(v.TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n"
//				+ "    FROM \r\n" + "        employee e\r\n" + "    JOIN \r\n"
//				+ "        voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "    JOIN \r\n"
//				+ "        grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "    WHERE \r\n"
//				+ "        v.INVOICE_TYPE = 'accommodation' and v.MANUAL_ENTRY='Y'\r\n" + "    GROUP BY \r\n"
//				+ "        e.EMPLOYEE_NAME, e.EMPLOYEE_CODE, g.GRADE, 'accommodation'\r\n" + "\r\n" + "   UNION ALL\r\n"
//				+ "    SELECT \r\n" + "        e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
//				+ "        e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "        g.GRADE AS GRADE,\r\n"
//				+ "        'accommodation' AS INVOICE_TYPE,\r\n" + "        SUM(hvd.AMOUNT) AS TOTAL_AMOUNT\r\n"
//				+ "    FROM \r\n" + "        employee e\r\n" + "    JOIN \r\n"
//				+ "        voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "    JOIN \r\n"
//				+ "        grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "    JOIN \r\n"
//				+ "        hotel_voucher hv ON hv.VOUCHER_ID = v.VOUCHER_ID\r\n" + "    JOIN \r\n"
//				+ "        hotel_voucher_detail hvd ON hv.HOTEL_VOUCHER_ID = hvd.HOTEL_VOUCHER_ID\r\n"
//				+ "    WHERE \r\n" + "        hvd.BILL_TYPE = 'accommodation'\r\n" + "    GROUP BY \r\n"
//				+ "        e.EMPLOYEE_NAME, e.EMPLOYEE_CODE, g.GRADE, 'accommodation'\r\n" + ") hotel_temporary\r\n"
//				+ "GROUP BY EMPLOYEE_NAME, EMPLOYEE_CODE, GRADE, INVOICE_TYPE; ";
//		RoleWiseReportDto roleWiseReportDto = new RoleWiseReportDto();
//
//		ExpenseReportDto expenseReportDto = null;
////		String previousName = null;
//		List<RoleWiseReportDto> roleWiseReportDtos = new ArrayList<RoleWiseReportDto>();
//		List<ExpenseReportDto> listExpenseReportDtos = new ArrayList<ExpenseReportDto>();
//		try (
//				// Establish a connection
//				Connection connection = DriverManager.getConnection(url, user, password);
//
//				// Create a prepared statement
//				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//			// Set the parameter value
////			preparedStatement.setInt(1, employeeID);
//			try (ResultSet resultSet = preparedStatement.executeQuery()) {
//
//				while (resultSet.next()) {
//					if (!resultSet.getString("EMPLOYEE_NAME").equalsIgnoreCase(roleWiseReportDto.getEmployeeName())) {
//						roleWiseReportDto = new RoleWiseReportDto();
//						roleWiseReportDto.setEmployeeName(resultSet.getString("EMPLOYEE_NAME"));
//						roleWiseReportDto.setEmployeeCode(resultSet.getString("EMPLOYEE_CODE"));
//						roleWiseReportDto.setEmployeeGrade(resultSet.getString("GRADE"));
//					}
//					if (resultSet.getString("INVOICE_TYPE").equalsIgnoreCase(XpendeskConstants.ACCOMMODATION_TYPE)) {
//						expenseReportDto = new ExpenseReportDto();
//						expenseReportDto.setAmount(resultSet.getString("TOTAL_AMOUNT"));
//						expenseReportDto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
//						expenseReportDto.setBillType(resultSet.getString("INVOICE_TYPE"));
//						listExpenseReportDtos.add(expenseReportDto);
//					}
//					if (resultSet.getString("INVOICE_TYPE").equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)) {
//						expenseReportDto = new ExpenseReportDto();
//						expenseReportDto.setAmount(resultSet.getString("TOTAL_AMOUNT"));
//						expenseReportDto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
//						expenseReportDto.setBillType(resultSet.getString("INVOICE_TYPE"));
//						listExpenseReportDtos.add(expenseReportDto);
//					}
//					if (resultSet.getString("INVOICE_TYPE").equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)) {
//						expenseReportDto = new ExpenseReportDto();
//						expenseReportDto.setAmount(resultSet.getString("TOTAL_AMOUNT"));
//						expenseReportDto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
//						expenseReportDto.setBillType(resultSet.getString("INVOICE_TYPE"));
//						listExpenseReportDtos.add(expenseReportDto);
//					}
//					roleWiseReportDtos.removeIf(roleWiseReport -> {
//						try {
//							return resultSet.getString("EMPLOYEE_NAME").equalsIgnoreCase(
//									roleWiseReport.getEmployeeName()) && !roleWiseReportDtos.isEmpty();
//						} catch (SQLException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						return false;
//					});
//					roleWiseReportDtos.add(roleWiseReportDto);
//					roleWiseReportDto.setEmployeeExpenses(listExpenseReportDtos);
//				}
//			} catch (SQLException e) {
//				// TODO: handle exception
//				throw new SQLException("Something Went Wrong...");
//			}
//		} catch (SQLException e) {
//			// TODO: handle exception
//			throw new SQLException("Something Went Wrong...");
//		}
//		return roleWiseReportDtos;
//	}

	@Override
	public List<RoleWiseReportDto> generateReportForAllUsers() throws SQLException {

		String query = "SELECT \r\n" + "    e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
				+ "    e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "    g.GRADE AS GRADE,\r\n"
				+ "    v.VOUCHER_DATE as VOUCHER_DATE,\r\n" + "    'conveyance' AS INVOICE_TYPE,\r\n"
				+ "    SUM(v.TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n" + "FROM \r\n" + "    employee e\r\n" + "JOIN \r\n"
				+ "    voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "JOIN \r\n"
				+ "    grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "WHERE \r\n" + "    v.INVOICE_TYPE = 'conveyance'\r\n"
				+ "GROUP BY \r\n" + "    e.EMPLOYEE_NAME, e.EMPLOYEE_CODE, v.VOUCHER_DATE, g.GRADE\r\n" + "	\r\n"
				+ "UNION ALL\r\n" + "\r\n" + "SELECT \r\n" + "    EMPLOYEE_NAME,\r\n" + "    EMPLOYEE_CODE,\r\n"
				+ "    GRADE,\r\n" + "    VOUCHER_DATE,\r\n" + "    INVOICE_TYPE,\r\n"
				+ "    SUM(TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n" + "FROM (\r\n" + "    SELECT \r\n"
				+ "        e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n" + "        e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n"
				+ "        g.GRADE AS GRADE,\r\n" + "        v.VOUCHER_DATE AS VOUCHER_DATE,\r\n"
				+ "        'food' AS INVOICE_TYPE,\r\n" + "        SUM(v.TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n"
				+ "    FROM \r\n" + "        employee e\r\n" + "    JOIN \r\n"
				+ "        voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "    JOIN \r\n"
				+ "        grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "    WHERE \r\n"
				+ "        v.INVOICE_TYPE = 'food' \r\n" + "    GROUP BY \r\n"
				+ "        e.EMPLOYEE_NAME, e.EMPLOYEE_CODE, v.VOUCHER_DATE, g.GRADE, 'food'\r\n" + "    UNION ALL\r\n"
				+ "    SELECT \r\n" + "        e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
				+ "        e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "        g.GRADE AS GRADE,\r\n"
				+ "        fvd.ITEM_DATE AS VOUCHER_DATE, \r\n" + "        'food' AS INVOICE_TYPE,\r\n"
				+ "        SUM(fvd.AMOUNT) AS TOTAL_AMOUNT\r\n" + "    FROM \r\n" + "        employee e\r\n"
				+ "    JOIN \r\n" + "        voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "    JOIN \r\n"
				+ "        grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "    JOIN \r\n"
				+ "        hotel_voucher hv ON hv.VOUCHER_ID = v.VOUCHER_ID\r\n" + "    JOIN \r\n"
				+ "        food_voucher_detail fvd ON hv.HOTEL_VOUCHER_ID = fvd.HOTEL_VOUCHER_ID\r\n" + "    WHERE \r\n"
				+ "        v.INVOICE_TYPE = 'hotel' \r\n" + "    GROUP BY \r\n"
				+ "        e.EMPLOYEE_NAME, e.EMPLOYEE_CODE, fvd.ITEM_DATE, g.GRADE, 'food'\r\n"
				+ ") food_temporary\r\n"
				+ "GROUP BY EMPLOYEE_NAME, EMPLOYEE_CODE, VOUCHER_DATE, GRADE, INVOICE_TYPE\r\n" + "\r\n"
				+ "UNION ALL\r\n" + "\r\n" + "SELECT \r\n" + "    EMPLOYEE_NAME,\r\n" + "    EMPLOYEE_CODE,\r\n"
				+ "    GRADE,\r\n" + "    VOUCHER_DATE,\r\n" + "    INVOICE_TYPE,\r\n"
				+ "    SUM(TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n" + "FROM (\r\n" + "    SELECT \r\n"
				+ "        e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n" + "        e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n"
				+ "        g.GRADE AS GRADE,\r\n" + "        v.VOUCHER_DATE AS VOUCHER_DATE,\r\n"
				+ "        'accommodation' AS INVOICE_TYPE,\r\n" + "        SUM(v.TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n"
				+ "    FROM \r\n" + "        employee e\r\n" + "    JOIN \r\n"
				+ "        voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "    JOIN \r\n"
				+ "        grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "    WHERE \r\n"
				+ "        v.INVOICE_TYPE = 'accommodation' and v.MANUAL_ENTRY='Y'\r\n" + "    GROUP BY \r\n"
				+ "        e.EMPLOYEE_NAME, e.EMPLOYEE_CODE, v.VOUCHER_DATE, g.GRADE, 'accommodation'\r\n" + "\r\n"
				+ "   UNION ALL\r\n" + "   \r\n" + "    SELECT \r\n" + "e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
				+ "        e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "g.GRADE AS GRADE,\r\n"
				+ "        hvd.ITEM_DATE AS VOUCHER_DATE,\r\n" + "'accommodation' AS INVOICE_TYPE,\r\n"
				+ "        SUM(hvd.AMOUNT) AS TOTAL_AMOUNT\r\n" + "FROM \r\n" + "employee e\r\n" + "    JOIN \r\n"
				+ "        voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "    JOIN \r\n"
				+ "        grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "    JOIN \r\n"
				+ "        hotel_voucher hv ON hv.VOUCHER_ID = v.VOUCHER_ID\r\n" + "    JOIN \r\n"
				+ "        hotel_voucher_detail hvd ON hv.HOTEL_VOUCHER_ID = hvd.HOTEL_VOUCHER_ID\r\n"
				+ "    WHERE \r\n" + "        hvd.BILL_TYPE = 'accommodation' \r\n" + "    GROUP BY \r\n"
				+ "        e.EMPLOYEE_NAME, e.EMPLOYEE_CODE, hvd.ITEM_DATE, g.GRADE, 'accommodation'\r\n"
				+ ") hotel_temporary\r\n"
				+ "GROUP BY EMPLOYEE_NAME, EMPLOYEE_CODE, GRADE, VOUCHER_DATE,INVOICE_TYPE\r\n"
				+ "order by INVOICE_TYPE, VOUCHER_DATE desc";

		RoleWiseReportDto roleWiseReportDto = new RoleWiseReportDto();
		ExpenseReportDto expenseReportDto = null;
		List<RoleWiseReportDto> roleWiseReportDtos = new ArrayList<RoleWiseReportDto>();
		List<ExpenseReportDto> listExpenseReportDtos = new ArrayList<ExpenseReportDto>();
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//			preparedStatement.setInt(1, employeeID);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					if (!resultSet.getString("EMPLOYEE_NAME").equalsIgnoreCase(roleWiseReportDto.getEmployeeName())) {
						roleWiseReportDto = new RoleWiseReportDto();
						roleWiseReportDto.setEmployeeName(resultSet.getString("EMPLOYEE_NAME"));
						roleWiseReportDto.setEmployeeCode(resultSet.getString("EMPLOYEE_CODE"));
						roleWiseReportDto.setEmployeeGrade(resultSet.getString("GRADE"));

					}
					if (resultSet.getString("INVOICE_TYPE").equalsIgnoreCase(XpendeskConstants.ACCOMMODATION_TYPE)) {
						expenseReportDto = new ExpenseReportDto();
						expenseReportDto.setAmount(resultSet.getString("TOTAL_AMOUNT"));
						expenseReportDto
								.setDate(new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("VOUCHER_DATE")));
						expenseReportDto.setBillType(resultSet.getString("INVOICE_TYPE"));
						listExpenseReportDtos.add(expenseReportDto);
					}
					if (resultSet.getString("INVOICE_TYPE").equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)) {
						expenseReportDto = new ExpenseReportDto();
						expenseReportDto.setAmount(resultSet.getString("TOTAL_AMOUNT"));
						expenseReportDto
								.setDate(new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("VOUCHER_DATE")));
						expenseReportDto.setBillType(resultSet.getString("INVOICE_TYPE"));
						listExpenseReportDtos.add(expenseReportDto);
					}
					if (resultSet.getString("INVOICE_TYPE").equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)) {
						expenseReportDto = new ExpenseReportDto();
						expenseReportDto.setAmount(resultSet.getString("TOTAL_AMOUNT"));
						expenseReportDto
								.setDate(new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("VOUCHER_DATE")));
						expenseReportDto.setBillType(resultSet.getString("INVOICE_TYPE"));
						listExpenseReportDtos.add(expenseReportDto);
					}
					roleWiseReportDtos.removeIf(roleWiseReport -> {
						try {
							return resultSet.getString("EMPLOYEE_NAME").equalsIgnoreCase(
									roleWiseReport.getEmployeeName()) && !roleWiseReportDtos.isEmpty();
						} catch (SQLException e) {

							e.printStackTrace();

						}
						return false;
					});
					roleWiseReportDtos.add(roleWiseReportDto);
					roleWiseReportDto.setEmployeeExpenses(listExpenseReportDtos);
				}
			} catch (SQLException e) {
				throw new SQLException("Something Went Wrong...");
			}
		} catch (SQLException e) {
			throw new SQLException("Something Went Wrong...");
		}

		return roleWiseReportDtos;
	}

	@Override
	public List<RoleWiseReportDto> getReportForUser(Integer employeeId) throws SQLException {
		// TODO Auto-generated method stub

		String query = "\r\n" + "SELECT \r\n" + "    e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
				+ "    e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "    g.GRADE AS GRADE,\r\n"
				+ "    v.VOUCHER_DATE as VOUCHER_DATE,\r\n" + "    'conveyance' AS INVOICE_TYPE,\r\n"
				+ "    SUM(v.TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n" + "FROM \r\n" + "    employee e\r\n" + "JOIN \r\n"
				+ "    voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "JOIN \r\n"
				+ "    grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "WHERE \r\n"
				+ "    v.INVOICE_TYPE = 'conveyance' and e.EMPLOYEE_ID = ?\r\n" + "GROUP BY \r\n"
				+ "    e.EMPLOYEE_NAME, e.EMPLOYEE_CODE, v.VOUCHER_DATE, g.GRADE\r\n" + "UNION ALL\r\n" + "\r\n"
				+ "\r\n" + "SELECT \r\n" + "    EMPLOYEE_NAME,\r\n" + "    EMPLOYEE_CODE,\r\n" + "    GRADE,\r\n"
				+ "    VOUCHER_DATE,\r\n" + "    INVOICE_TYPE,\r\n" + "    SUM(TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n"
				+ "FROM (\r\n" + "    SELECT \r\n" + "        e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
				+ "        e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "        g.GRADE AS GRADE,\r\n"
				+ "        v.VOUCHER_DATE AS VOUCHER_DATE,\r\n" + "        'food' AS INVOICE_TYPE,\r\n"
				+ "        SUM(v.TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n" + "    FROM \r\n" + "        employee e\r\n"
				+ "    JOIN \r\n" + "        voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "    JOIN \r\n"
				+ "        grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "    WHERE \r\n"
				+ "        v.INVOICE_TYPE = 'food' and e.EMPLOYEE_ID = ?\r\n" + "    GROUP BY \r\n"
				+ "        e.EMPLOYEE_NAME, e.EMPLOYEE_CODE, v.VOUCHER_DATE, g.GRADE, 'food' \r\n" + "    UNION ALL\r\n"
				+ "    SELECT \r\n" + "        e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
				+ "        e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "        g.GRADE AS GRADE,\r\n"
				+ "        fvd.ITEM_DATE AS VOUCHER_DATE, \r\n" + "        'food' AS INVOICE_TYPE,\r\n"
				+ "        SUM(fvd.AMOUNT) AS TOTAL_AMOUNT\r\n" + "    FROM \r\n" + "        employee e\r\n"
				+ "    JOIN \r\n" + "        voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "    JOIN \r\n"
				+ "        grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "    JOIN \r\n"
				+ "        hotel_voucher hv ON hv.VOUCHER_ID = v.VOUCHER_ID\r\n" + "    JOIN \r\n"
				+ "        food_voucher_detail fvd ON hv.HOTEL_VOUCHER_ID = fvd.HOTEL_VOUCHER_ID\r\n" + "    WHERE \r\n"
				+ "        v.INVOICE_TYPE = 'hotel' and e.EMPLOYEE_ID = ?\r\n" + "    GROUP BY \r\n"
				+ "        e.EMPLOYEE_NAME, e.EMPLOYEE_CODE, fvd.ITEM_DATE, g.GRADE, 'food'\r\n"
				+ ") food_temporary\r\n"
				+ "GROUP BY EMPLOYEE_NAME, EMPLOYEE_CODE, VOUCHER_DATE, GRADE, INVOICE_TYPE\r\n" + "UNION ALL\r\n"
				+ "\r\n" + "SELECT \r\n" + "    EMPLOYEE_NAME,\r\n" + "    EMPLOYEE_CODE,\r\n" + "    GRADE,\r\n"
				+ "    VOUCHER_DATE,\r\n" + "    INVOICE_TYPE,\r\n" + "    SUM(TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n"
				+ "FROM (\r\n" + "    SELECT \r\n" + "        e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
				+ "        e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "        g.GRADE AS GRADE,\r\n"
				+ "        v.VOUCHER_DATE AS VOUCHER_DATE,\r\n" + "        'accommodation' AS INVOICE_TYPE,\r\n"
				+ "        SUM(v.TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n" + "    FROM \r\n" + "        employee e\r\n"
				+ "    JOIN \r\n" + "        voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "    JOIN \r\n"
				+ "        grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "    WHERE \r\n"
				+ "        v.INVOICE_TYPE = 'accommodation' and v.MANUAL_ENTRY='Y' and e.EMPLOYEE_ID = ?\r\n"
				+ "    GROUP BY \r\n"
				+ "        e.EMPLOYEE_NAME, e.EMPLOYEE_CODE, v.VOUCHER_DATE, g.GRADE, 'accommodation'\r\n" + "\r\n"
				+ "   UNION ALL\r\n" + "   \r\n" + "    SELECT \r\n" + "        e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
				+ "        e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "        g.GRADE AS GRADE,\r\n"
				+ "        hvd.ITEM_DATE AS VOUCHER_DATE,\r\n" + "        'accommodation' AS INVOICE_TYPE,\r\n"
				+ "        SUM(hvd.AMOUNT) AS TOTAL_AMOUNT\r\n" + "    FROM \r\n" + "        employee e\r\n"
				+ "    JOIN \r\n" + "        voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "    JOIN \r\n"
				+ "        grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "    JOIN \r\n"
				+ "        hotel_voucher hv ON hv.VOUCHER_ID = v.VOUCHER_ID\r\n" + "    JOIN \r\n"
				+ "        hotel_voucher_detail hvd ON hv.HOTEL_VOUCHER_ID = hvd.HOTEL_VOUCHER_ID\r\n"
				+ "    WHERE \r\n" + "        hvd.BILL_TYPE = 'accommodation' and e.EMPLOYEE_ID = ?\r\n"
				+ "    GROUP BY \r\n"
				+ "        e.EMPLOYEE_NAME, e.EMPLOYEE_CODE, hvd.ITEM_DATE, g.GRADE, 'accommodation'\r\n"
				+ ") hotel_temporary\r\n"
				+ "GROUP BY EMPLOYEE_NAME, EMPLOYEE_CODE, GRADE, VOUCHER_DATE,INVOICE_TYPE\r\n"
				+ "order by INVOICE_TYPE, VOUCHER_DATE desc;";
		RoleWiseReportDto roleWiseReportDto = new RoleWiseReportDto();

		ExpenseReportDto expenseReportDto = null;
//		String previousName = null;
		List<RoleWiseReportDto> roleWiseReportDtos = new ArrayList<RoleWiseReportDto>();
		List<ExpenseReportDto> listExpenseReportDtos = new ArrayList<ExpenseReportDto>();
		try (
				// Establish a connection
				Connection connection = DriverManager.getConnection(url, user, password);

				// Create a prepared statement
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			// Set the parameter value

			preparedStatement.setInt(1, employeeId);
			preparedStatement.setInt(2, employeeId);
			preparedStatement.setInt(3, employeeId);
			preparedStatement.setInt(4, employeeId);
			preparedStatement.setInt(5, employeeId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				while (resultSet.next()) {
					if (!resultSet.getString("EMPLOYEE_NAME").equalsIgnoreCase(roleWiseReportDto.getEmployeeName())) {
						roleWiseReportDto = new RoleWiseReportDto();
						roleWiseReportDto.setEmployeeId(employeeId);
						roleWiseReportDto.setEmployeeName(resultSet.getString("EMPLOYEE_NAME"));
						roleWiseReportDto.setEmployeeCode(resultSet.getString("EMPLOYEE_CODE"));
						roleWiseReportDto.setEmployeeGrade(resultSet.getString("GRADE"));
					}
					if (resultSet.getString("INVOICE_TYPE").equalsIgnoreCase(XpendeskConstants.ACCOMMODATION_TYPE)) {
						expenseReportDto = new ExpenseReportDto();
						expenseReportDto.setAmount(resultSet.getString("TOTAL_AMOUNT"));
						expenseReportDto.setDate(resultSet.getString("VOUCHER_DATE"));
						expenseReportDto.setBillType(resultSet.getString("INVOICE_TYPE"));
						listExpenseReportDtos.add(expenseReportDto);
					}
					if (resultSet.getString("INVOICE_TYPE").equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)) {
						expenseReportDto = new ExpenseReportDto();
						expenseReportDto.setAmount(resultSet.getString("TOTAL_AMOUNT"));
						expenseReportDto.setDate(resultSet.getString("VOUCHER_DATE"));
						expenseReportDto.setBillType(resultSet.getString("INVOICE_TYPE"));
						listExpenseReportDtos.add(expenseReportDto);
					}
					if (resultSet.getString("INVOICE_TYPE").equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)) {
						expenseReportDto = new ExpenseReportDto();
						expenseReportDto.setAmount(resultSet.getString("TOTAL_AMOUNT"));
						expenseReportDto.setDate(resultSet.getString("VOUCHER_DATE"));
						expenseReportDto.setBillType(resultSet.getString("INVOICE_TYPE"));
						listExpenseReportDtos.add(expenseReportDto);
					}
					roleWiseReportDtos.removeIf(roleWiseReport -> {
						try {
							return resultSet.getString("EMPLOYEE_NAME").equalsIgnoreCase(
									roleWiseReport.getEmployeeName()) && !roleWiseReportDtos.isEmpty();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return false;
					});
					roleWiseReportDtos.add(roleWiseReportDto);
					roleWiseReportDto.setEmployeeExpenses(listExpenseReportDtos);
				}
			} catch (SQLException e) {
				// TODO: handle exception
				throw new SQLException("Something Went Wrong...");
			}
		} catch (SQLException e) {
			// TODO: handle exception
			throw new SQLException("Something Went Wrong...");
		}
		return roleWiseReportDtos;
	}

	@Override
	public List<RoleWiseReportDto> generateReportForUsers(Integer employeeId) throws SQLException {
		// TODO Auto-generated method stub

		String query = "SELECT\r\n" + "	e.EMPLOYEE_ID AS EMPLOYEE_ID,\r\n" + "    e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
				+ "    e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "    g.GRADE AS GRADE,\r\n"
				+ "    v.VOUCHER_DATE as VOUCHER_DATE,\r\n" + "    'conveyance' AS INVOICE_TYPE,\r\n"
				+ "    SUM(v.TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n" + "FROM \r\n" + "    employee e\r\n" + "JOIN \r\n"
				+ "    voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "JOIN \r\n"
				+ "    grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "WHERE \r\n"
				+ "    v.INVOICE_TYPE = 'conveyance' AND e.MANAGER_ID = ?\r\n" + "GROUP BY \r\n"
				+ "    e.EMPLOYEE_NAME, e.EMPLOYEE_ID, e.EMPLOYEE_CODE, v.VOUCHER_DATE, g.GRADE\r\n" + "\r\n"
				+ "UNION ALL\r\n" + "\r\n" + "SELECT \r\n" + "	EMPLOYEE_ID,\r\n" + "    EMPLOYEE_NAME,\r\n"
				+ "    EMPLOYEE_CODE,\r\n" + "    GRADE,\r\n" + "    VOUCHER_DATE,\r\n" + "    INVOICE_TYPE,\r\n"
				+ "    SUM(TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n" + "FROM (\r\n" + "    SELECT \r\n"
				+ "		e.EMPLOYEE_ID AS EMPLOYEE_ID,\r\n" + "        e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
				+ "        e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "        g.GRADE AS GRADE,\r\n"
				+ "        v.VOUCHER_DATE AS VOUCHER_DATE,\r\n" + "        'food' AS INVOICE_TYPE,\r\n"
				+ "        SUM(v.TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n" + "    FROM \r\n" + "        employee e\r\n"
				+ "    JOIN \r\n" + "        voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "    JOIN \r\n"
				+ "        grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "    WHERE \r\n"
				+ "        v.INVOICE_TYPE = 'food' AND e.MANAGER_ID = ?\r\n" + "    GROUP BY \r\n"
				+ "        e.EMPLOYEE_NAME, e.EMPLOYEE_ID, e.EMPLOYEE_CODE, v.VOUCHER_DATE, g.GRADE, 'food'\r\n"
				+ "\r\n" + "    UNION ALL\r\n" + "    \r\n" + "    SELECT \r\n"
				+ "		e.EMPLOYEE_ID AS EMPLOYEE_ID,\r\n" + "        e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
				+ "        e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "        g.GRADE AS GRADE,\r\n"
				+ "        fvd.ITEM_DATE AS VOUCHER_DATE, \r\n" + "        'food' AS INVOICE_TYPE,\r\n"
				+ "        SUM(fvd.AMOUNT) AS TOTAL_AMOUNT\r\n" + "    FROM \r\n" + "        employee e\r\n"
				+ "    JOIN \r\n" + "        voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "    JOIN \r\n"
				+ "        grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "    JOIN \r\n"
				+ "        hotel_voucher hv ON hv.VOUCHER_ID = v.VOUCHER_ID\r\n" + "    JOIN \r\n"
				+ "        food_voucher_detail fvd ON hv.HOTEL_VOUCHER_ID = fvd.HOTEL_VOUCHER_ID\r\n" + "    WHERE \r\n"
				+ "        v.INVOICE_TYPE = 'hotel' AND e.MANAGER_ID = ?\r\n" + "    GROUP BY \r\n"
				+ "        e.EMPLOYEE_NAME, e.EMPLOYEE_ID, e.EMPLOYEE_CODE, fvd.ITEM_DATE, g.GRADE, 'food'\r\n"
				+ ") food_temporary\r\n"
				+ "GROUP BY EMPLOYEE_NAME, EMPLOYEE_ID, EMPLOYEE_CODE, VOUCHER_DATE, GRADE, INVOICE_TYPE\r\n" + "\r\n"
				+ "UNION ALL\r\n" + "\r\n" + "SELECT \r\n" + "	EMPLOYEE_ID,\r\n" + "    EMPLOYEE_NAME,\r\n"
				+ "    EMPLOYEE_CODE,\r\n" + "    GRADE,\r\n" + "    VOUCHER_DATE,\r\n" + "    INVOICE_TYPE,\r\n"
				+ "    SUM(TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n" + "FROM (\r\n" + "    SELECT \r\n"
				+ "		e.EMPLOYEE_ID AS EMPLOYEE_ID,\r\n" + "		e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
				+ "        e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "        g.GRADE AS GRADE,\r\n"
				+ "        v.VOUCHER_DATE AS VOUCHER_DATE,\r\n" + "        'accommodation' AS INVOICE_TYPE,\r\n"
				+ "        SUM(v.TOTAL_AMOUNT) AS TOTAL_AMOUNT\r\n" + "    FROM \r\n" + "        employee e\r\n"
				+ "    JOIN \r\n" + "        voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "    JOIN \r\n"
				+ "        grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "    WHERE \r\n"
				+ "        v.INVOICE_TYPE = 'accommodation' AND v.MANUAL_ENTRY='Y' AND e.MANAGER_ID = ?\r\n"
				+ "    GROUP BY \r\n"
				+ "        e.EMPLOYEE_NAME, e.EMPLOYEE_ID, e.EMPLOYEE_CODE, v.VOUCHER_DATE, g.GRADE, 'accommodation'\r\n"
				+ "\r\n" + "   UNION ALL\r\n" + "   \r\n" + "    SELECT \r\n"
				+ "		e.EMPLOYEE_ID AS EMPLOYEE_ID,\r\n" + "        e.EMPLOYEE_NAME AS EMPLOYEE_NAME,\r\n"
				+ "        e.EMPLOYEE_CODE AS EMPLOYEE_CODE,\r\n" + "        g.GRADE AS GRADE,\r\n"
				+ "        hvd.ITEM_DATE AS VOUCHER_DATE,\r\n" + "        'accommodation' AS INVOICE_TYPE,\r\n"
				+ "        SUM(hvd.AMOUNT) AS TOTAL_AMOUNT\r\n" + "    FROM \r\n" + "        employee e\r\n"
				+ "    JOIN \r\n" + "        voucher v ON e.EMPLOYEE_ID = v.EMPLOYEE_ID\r\n" + "    JOIN \r\n"
				+ "        grade g ON e.GRADE_ID = g.GRADE_ID\r\n" + "    JOIN \r\n"
				+ "        hotel_voucher hv ON hv.VOUCHER_ID = v.VOUCHER_ID\r\n" + "    JOIN \r\n"
				+ "        hotel_voucher_detail hvd ON hv.HOTEL_VOUCHER_ID = hvd.HOTEL_VOUCHER_ID\r\n"
				+ "    WHERE \r\n" + "        hvd.BILL_TYPE = 'accommodation' AND e.MANAGER_ID = ?\r\n"
				+ "    GROUP BY \r\n"
				+ "        e.EMPLOYEE_NAME, e.EMPLOYEE_ID, e.EMPLOYEE_CODE, hvd.ITEM_DATE, g.GRADE, 'accommodation'\r\n"
				+ ") hotel_temporary\r\n"
				+ "GROUP BY EMPLOYEE_NAME, EMPLOYEE_ID, EMPLOYEE_CODE, GRADE, VOUCHER_DATE, INVOICE_TYPE\r\n" + "\r\n"
				+ "ORDER BY INVOICE_TYPE, VOUCHER_DATE DESC;";

		RoleWiseReportDto roleWiseReportDto = new RoleWiseReportDto();

		ExpenseReportDto expenseReportDto = null;
//		String previousName = null;
		List<RoleWiseReportDto> roleWiseReportDtos = new ArrayList<RoleWiseReportDto>();
		List<ExpenseReportDto> listExpenseReportDtos = null;
		try (
				// Establish a connection
				Connection connection = DriverManager.getConnection(url, user, password);

				// Create a prepared statement
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			// Set the parameter value

			preparedStatement.setInt(1, employeeId);
			preparedStatement.setInt(2, employeeId);
			preparedStatement.setInt(3, employeeId);
			preparedStatement.setInt(4, employeeId);
			preparedStatement.setInt(5, employeeId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				while (resultSet.next()) {
					if (!resultSet.getString("EMPLOYEE_NAME").equalsIgnoreCase(roleWiseReportDto.getEmployeeName()))
						listExpenseReportDtos = new ArrayList<ExpenseReportDto>();
//					if (!resultSet.getString("EMPLOYEE_NAME").equalsIgnoreCase(roleWiseReportDto.getEmployeeName())) {
					roleWiseReportDto = new RoleWiseReportDto();
					roleWiseReportDto.setEmployeeId(resultSet.getInt("EMPLOYEE_ID"));
					roleWiseReportDto.setEmployeeName(resultSet.getString("EMPLOYEE_NAME"));
					roleWiseReportDto.setEmployeeCode(resultSet.getString("EMPLOYEE_CODE"));
					roleWiseReportDto.setEmployeeGrade(resultSet.getString("GRADE"));
//					}

					if (resultSet.getString("INVOICE_TYPE").equalsIgnoreCase(XpendeskConstants.ACCOMMODATION_TYPE)
							&& resultSet.getString("EMPLOYEE_NAME")
									.equalsIgnoreCase(roleWiseReportDto.getEmployeeName())) {
						expenseReportDto = new ExpenseReportDto();
						expenseReportDto.setAmount(resultSet.getString("TOTAL_AMOUNT"));
						expenseReportDto.setDate(resultSet.getString("VOUCHER_DATE"));
						expenseReportDto.setBillType(resultSet.getString("INVOICE_TYPE"));
						listExpenseReportDtos.add(expenseReportDto);
					}
					if (resultSet.getString("INVOICE_TYPE").equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)
							&& resultSet.getString("EMPLOYEE_NAME")
									.equalsIgnoreCase(roleWiseReportDto.getEmployeeName())) {
						expenseReportDto = new ExpenseReportDto();
						expenseReportDto.setAmount(resultSet.getString("TOTAL_AMOUNT"));
						expenseReportDto.setDate(resultSet.getString("VOUCHER_DATE"));
						expenseReportDto.setBillType(resultSet.getString("INVOICE_TYPE"));
						listExpenseReportDtos.add(expenseReportDto);
					}
					if (resultSet.getString("INVOICE_TYPE").equalsIgnoreCase(XpendeskConstants.FOOD_TYPE) && resultSet
							.getString("EMPLOYEE_NAME").equalsIgnoreCase(roleWiseReportDto.getEmployeeName())) {
						expenseReportDto = new ExpenseReportDto();
						expenseReportDto.setAmount(resultSet.getString("TOTAL_AMOUNT"));
						expenseReportDto.setDate(resultSet.getString("VOUCHER_DATE"));
						expenseReportDto.setBillType(resultSet.getString("INVOICE_TYPE"));
						listExpenseReportDtos.add(expenseReportDto);
					}

					roleWiseReportDtos.removeIf(roleWiseReport -> {
						try {
							return resultSet.getString("EMPLOYEE_NAME").equalsIgnoreCase(
									roleWiseReport.getEmployeeName()) && !roleWiseReportDtos.isEmpty();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return false;
					});
					roleWiseReportDtos.add(roleWiseReportDto);
					roleWiseReportDto.setEmployeeExpenses(listExpenseReportDtos);
				}
			} catch (SQLException e) {
				// TODO: handle exception
				throw new SQLException("Something Went Wrong...");
			}
		} catch (SQLException e) {
			// TODO: handle exception
			throw new SQLException("Something Went Wrong...");
		}
		return roleWiseReportDtos;
	}

}
