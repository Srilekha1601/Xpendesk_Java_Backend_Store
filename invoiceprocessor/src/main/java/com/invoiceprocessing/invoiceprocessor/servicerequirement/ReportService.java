package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.response.BarChartReportUserExpenseDto;
import com.invoiceprocessing.invoiceprocessor.response.EmployeeDto;
import com.invoiceprocessing.invoiceprocessor.response.ExpenseForEmployeeBarChartReportDto;
import com.invoiceprocessing.invoiceprocessor.response.MonthWiseExpenseDto;
import com.invoiceprocessing.invoiceprocessor.response.ReportDto;
import com.invoiceprocessing.invoiceprocessor.response.ReviewerReportForDonutChartDto;
import com.invoiceprocessing.invoiceprocessor.response.SubmittedDataInformation;
import com.invoiceprocessing.invoiceprocessor.response.YearWiseReport;
import com.invoiceprocessing.invoiceprocessor.response.YearlyTotalExpenses;

public interface ReportService {

	ReportDto getAllReportByTripID(Integer tripID);

	SubmittedDataInformation getAllSubmittedData(Integer employeeID);

	MonthWiseExpenseDto monthlyDataSummary(Integer employeeID, Integer year);

	YearWiseReport reportGenerationInMonthAndYear(Integer employeeID, Integer year);

	YearlyTotalExpenses yearlyExpenses(Integer employeeID, Integer year);

//	........ Report For Reviewer Section .........

	ReviewerReportForDonutChartDto getDonutChartReportForReviewer(EmployeeDto employee);

	List<BarChartReportUserExpenseDto> getBarChartReportForUserExpense(
			BarChartReportUserExpenseDto barChartReportUserExpenseDto);

	List<ExpenseForEmployeeBarChartReportDto> barChartReportForCheckIndividualUsers(
			ExpenseForEmployeeBarChartReportDto expenseForEmployeeBarChartReportDto);

}
