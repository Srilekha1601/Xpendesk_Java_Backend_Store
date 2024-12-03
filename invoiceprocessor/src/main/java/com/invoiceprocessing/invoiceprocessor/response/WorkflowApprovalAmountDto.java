package com.invoiceprocessing.invoiceprocessor.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowApprovalAmountDto {
	
	private Integer tripConsolidationId;
	
	private BigDecimal approvedAmount;
	
}
