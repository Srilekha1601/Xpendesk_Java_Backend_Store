package com.invoiceprocessing.invoiceprocessor.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkFlowStepDto {

	private Integer stepID;

	private String stepName;

	private Integer stepSequence;

	private Integer approverRoleID;

	private Integer backUpApproverRoleID;

}
