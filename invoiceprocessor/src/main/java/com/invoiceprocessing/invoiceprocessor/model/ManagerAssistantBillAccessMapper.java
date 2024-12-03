package com.invoiceprocessing.invoiceprocessor.model;

import java.sql.Timestamp;

import com.invoiceprocessing.invoiceprocessor.service.XpendeskEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EntityListeners(XpendeskEntityListener.class)
@Table(name = "MANAGER_ASSISTANT_BILL_ACCESS_MAPPER")
public class ManagerAssistantBillAccessMapper extends AuditFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MAPPER_ID")
	private Integer mapperId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MANAGER_ID", referencedColumnName = "EMPLOYEE_ID")
	private Employee manager;

	@Column(name = "ASSISTANT_ID")
	private Integer assistantId;

	@Column(name = "ASSISTANT_NAME", length = 50)
	private String assistantName;

	@Column(name = "ASSISTANT_ACCESS", length = 1)
	private String assistantAccess;

	@Column(name = "ASSISTANT_ACCESS_EXPIRY_DATE")
	private Timestamp assistantAccessExpiryDate;

}
