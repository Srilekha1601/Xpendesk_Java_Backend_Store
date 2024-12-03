package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.model.WorkflowTask;
import com.invoiceprocessing.invoiceprocessor.response.CostAndProjectDetailsCheckDto;
import com.invoiceprocessing.invoiceprocessor.response.PendingTaskDto;
import com.invoiceprocessing.invoiceprocessor.response.ReferenceDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.WorkflowTaskDto;

public interface WorkFlowService {

	public WorkflowTaskDto processNextWorkflowTask(WorkflowTaskDto workFlowTaskDto);

	public WorkflowTask processFirstWorkflowTask(ReferenceDto reference);

	public List<WorkflowTaskDto> getWorkflowTasks(WorkflowTaskDto workFlowTaskDto);

	public List<PendingTaskDto> pendingTasksForWorkflow(PendingTaskDto pendingTaskDto);

	public List<PendingTaskDto> pendingTasksForWorkflowForAnyNextLevel(PendingTaskDto pendingTaskDto);

	public CostAndProjectDetailsCheckDto getCostAndProjectDetails(VoucherDto voucherDto);

}
