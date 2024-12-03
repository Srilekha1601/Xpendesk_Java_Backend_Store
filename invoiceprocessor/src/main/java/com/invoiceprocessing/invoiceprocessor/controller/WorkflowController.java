package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.CostAndProjectDetailsCheckDto;
import com.invoiceprocessing.invoiceprocessor.response.PendingTaskDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.WorkflowTaskDto;
import com.invoiceprocessing.invoiceprocessor.service.WorkflowServiceImpl;

@RestController
@RequestMapping("/workflow")
public class WorkflowController {

	@Autowired
	WorkflowServiceImpl workFlowServiceImpl;

	@PostMapping("/save")
	public ResponseEntity<WorkflowTaskDto> processWorkflow(@RequestBody WorkflowTaskDto workFlowDto) {
		return ResponseEntity.ok(workFlowServiceImpl.processNextWorkflowTask(workFlowDto));
	}

	@PostMapping("/tasks")
	public ResponseEntity<List<WorkflowTaskDto>> getWorkflowTasks(@RequestBody WorkflowTaskDto workFlowDto) {
		return ResponseEntity.ok(workFlowServiceImpl.getWorkflowTasks(workFlowDto));
	}

	@PostMapping("/pendingtask")
	public ResponseEntity<List<PendingTaskDto>> pendingTasks(@RequestBody PendingTaskDto pendingTaskDto) {
		return ResponseEntity.ok(workFlowServiceImpl.pendingTasksForWorkflow(pendingTaskDto));
	}

	@PostMapping("/pendingtask-for-previous-levels")
	public ResponseEntity<List<PendingTaskDto>> pendingTasksForPreviousLevels(
			@RequestBody PendingTaskDto pendingTaskDto) {
		return ResponseEntity.ok(workFlowServiceImpl.pendingTasksForWorkflowForAnyNextLevel(pendingTaskDto));
	}

	@PostMapping("/fetch-project-cost-code")
	public ResponseEntity<CostAndProjectDetailsCheckDto> getProjectAndCostListDto(@RequestBody VoucherDto voucherDto) {
		return new ResponseEntity<CostAndProjectDetailsCheckDto>(
				workFlowServiceImpl.getCostAndProjectDetails(voucherDto), HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

}
