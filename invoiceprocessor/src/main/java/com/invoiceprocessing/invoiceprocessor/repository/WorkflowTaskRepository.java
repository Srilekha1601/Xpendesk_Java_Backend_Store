package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.WorkflowTask;

@Repository
public interface WorkflowTaskRepository extends JpaRepository<WorkflowTask, Integer> {
	public List<WorkflowTask> findByActionType(String actionType);

	List<WorkflowTask> findAllByReferenceIDOrderByTaskIdDesc(Integer referenceID);

}
