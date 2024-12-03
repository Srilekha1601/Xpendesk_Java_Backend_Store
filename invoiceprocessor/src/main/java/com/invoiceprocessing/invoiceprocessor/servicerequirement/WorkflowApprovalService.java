package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.response.ApprovalDto;

public interface WorkflowApprovalService {

	public List<ApprovalDto> approvalRequests(ApprovalDto employeeId);

}
