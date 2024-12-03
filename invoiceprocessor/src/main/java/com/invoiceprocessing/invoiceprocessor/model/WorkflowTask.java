package com.invoiceprocessing.invoiceprocessor.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "WORKFLOW_TASK")
public class WorkflowTask {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_id")
	private Integer taskId;

	@Column(name = "task_description", length = 100)
	private String taskDescription;

	@Column(name = "reference_id")
	private Integer referenceID;

	@Column(name = "reference_type")
	private String referenceType;

	@ManyToOne
	@JoinColumn(name = "step_id", referencedColumnName = "step_id")
	private WorkflowStep step;

	@ManyToOne
	@JoinColumn(name = "generated_by", referencedColumnName = "EMPLOYEE_ID")
	private Employee generatedBy;

	@Column(name = "generated_on")
	private Timestamp generatedOn;

	@ManyToOne
	@JoinColumn(name = "assigned_to", referencedColumnName = "EMPLOYEE_ID")
	private Employee assignedTo;

	@ManyToOne
	@JoinColumn(name = "executed_by", referencedColumnName = "EMPLOYEE_ID")
	private Employee executedBy;

	@Column(name = "executed_on")
	private Timestamp executedOn;

	@Column(name = "action_type")
	private String actionType;

	@Column(name = "comments", length = 100)
	private String comments;

	@Column(name = "mark_as_read", length = 1)
	private String markAsRead;

	@Column(name = "approval_id", length = 1)
	private String approvalId;

}
