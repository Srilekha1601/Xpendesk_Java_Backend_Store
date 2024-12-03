package com.invoiceprocessing.invoiceprocessor.response;

import lombok.Data;

@Data
public class ReviewerReportForDonutChartDto {

	private Integer pendingTask;

	private Integer approvedTask;

	private Integer rejectedTask;

}
