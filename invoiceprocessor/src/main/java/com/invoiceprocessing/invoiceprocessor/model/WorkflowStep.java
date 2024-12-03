package com.invoiceprocessing.invoiceprocessor.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "WORKFLOW_STEP")
public class WorkflowStep {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "step_id")
	private Integer stepID;

	@Column(name = "step_name", length = 50)
	private String stepName;

	@Column(name = "step_sequence")
	private Integer stepSequence;

	@Column(name = "voucher_amount_threshold")
	private BigDecimal voucherAmountThreshold;
	
	@ManyToOne
	@JoinColumn(name = "approver_role_id", referencedColumnName = "ROLE_ID")
	private Role approverRole;

	@ManyToOne
	@JoinColumn(name = "backup_approver_role_id", referencedColumnName = "ROLE_ID")
	private Role backUpApproverRole;
	
	@ManyToOne
	@JoinColumn(name = "workflow_id")
	private Workflow workflow;

}
