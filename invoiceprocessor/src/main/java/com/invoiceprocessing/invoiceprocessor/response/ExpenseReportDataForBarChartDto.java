package com.invoiceprocessing.invoiceprocessor.response;

import lombok.Data;

@Data
public class ExpenseReportDataForBarChartDto {

	private Integer totalPendingInvoices;

	private Integer totalApprovedInvoices;

	private Integer totalRejectedInvoices;

}
