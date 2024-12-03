package com.invoiceprocessing.invoiceprocessor.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "WORKFLOW")
public class Workflow {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "workflow_id")
	private Integer workflowId;

	@Column(name = "workflow_description", length = 100)
	private String workflowDescription;
	
	@OneToMany(mappedBy = "workflow", fetch = FetchType.EAGER)
	private List<WorkflowStep> workflowStepList;

}
