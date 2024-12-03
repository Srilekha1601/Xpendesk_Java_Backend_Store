package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.Workflow;
import com.invoiceprocessing.invoiceprocessor.model.WorkflowStep;

@Repository
public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, Integer> {
	
	public List<WorkflowStep> findByWorkflowAndStepSequence(Workflow workflow, Integer sequence);
}
