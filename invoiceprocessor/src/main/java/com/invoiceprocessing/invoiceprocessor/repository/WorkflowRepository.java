package com.invoiceprocessing.invoiceprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.invoiceprocessing.invoiceprocessor.model.Workflow;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Integer> {
	
}
