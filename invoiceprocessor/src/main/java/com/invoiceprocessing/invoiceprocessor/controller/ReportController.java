package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.BarChartReportUserExpenseDto;
import com.invoiceprocessing.invoiceprocessor.response.EmployeeDto;
import com.invoiceprocessing.invoiceprocessor.response.ExpenseForEmployeeBarChartReportDto;
import com.invoiceprocessing.invoiceprocessor.response.MonthWiseExpenseDto;
import com.invoiceprocessing.invoiceprocessor.response.ReportDto;
import com.invoiceprocessing.invoiceprocessor.response.ReviewerReportForDonutChartDto;
import com.invoiceprocessing.invoiceprocessor.response.SubmittedDataInformation;
import com.invoiceprocessing.invoiceprocessor.response.YearWiseReport;
import com.invoiceprocessing.invoiceprocessor.response.YearlyTotalExpenses;
import com.invoiceprocessing.invoiceprocessor.service.ReportServiceImpl;

@RestController
@RequestMapping("/generatreport")
public class ReportController {

	@Autowired
	ReportServiceImpl reportServiceImpl;

	@GetMapping("/getreport/{tripID}")
	public ResponseEntity<ReportDto> allReportInformation(@PathVariable Integer tripID) {
		return ResponseEntity.ok(reportServiceImpl.getAllReportByTripID(tripID));
	}

	@GetMapping("/getpiechartdata/{employeeID}")
	public ResponseEntity<SubmittedDataInformation> getAllSubmittedDataCount(@PathVariable Integer employeeID) {
		return ResponseEntity.ok(reportServiceImpl.getAllSubmittedData(employeeID));
	}

	@GetMapping("/getmonthlydata/{employeeID}/{year}")
	public ResponseEntity<MonthWiseExpenseDto> getMonthWiseData(@PathVariable Integer employeeID,
			@PathVariable Integer year) {
		return ResponseEntity.ok(reportServiceImpl.monthlyDataSummary(employeeID, year));
	}

	@GetMapping("/getinformationmonthwise/{employeeID}/{year}")
	public ResponseEntity<YearWiseReport> getInformationByChoice(@PathVariable Integer employeeID,
			@PathVariable Integer year) {
		return ResponseEntity.ok(reportServiceImpl.reportGenerationInMonthAndYear(employeeID, year));
	}

	@GetMapping("/yearlyExpenses/{employeeID}/{year}")
	public ResponseEntity<YearlyTotalExpenses> getTotalYearlyExpenses(@PathVariable Integer employeeID,
			@PathVariable Integer year) {
		return ResponseEntity.ok(reportServiceImpl.yearlyExpenses(employeeID, year));
	}

	// ......... Controllers For Reviewer ReportCharts ......... //

	@PostMapping("/getdonutchartreport")
	public ResponseEntity<ReviewerReportForDonutChartDto> getDonutChartReport(@RequestBody EmployeeDto employeeDto) {
		return ResponseEntity.ok(reportServiceImpl.getDonutChartReportForReviewer(employeeDto));
	}

	@PostMapping("/reportforbarchartuserexpense")
	public ResponseEntity<List<BarChartReportUserExpenseDto>> getBarChartReportForUserExpense(
			@RequestBody BarChartReportUserExpenseDto barChartReportUserExpenseDto) {
		return ResponseEntity.ok(reportServiceImpl.getBarChartReportForUserExpense(barChartReportUserExpenseDto));
	}

	@PostMapping("/barchartforindividualusers")
	public ResponseEntity<List<ExpenseForEmployeeBarChartReportDto>> barChartReportForIndividualUsers(
			@RequestBody ExpenseForEmployeeBarChartReportDto expenseForEmployeeBarChartReportDto) {
		return ResponseEntity
				.ok(reportServiceImpl.barChartReportForCheckIndividualUsers(expenseForEmployeeBarChartReportDto));
	}

}
