package com.invoiceprocessing.invoiceprocessor.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "COST_CODE")
public class CostCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cost_id")
	private Integer costId;

	@Column(name = "cost_code", length = 25)
	private String costCode;

	@Column(name = "cost_description")
	private String costDescription;

	@Column(name = "is_enabled", length = 1)
	private String isEnable;

	@Column(name = "valid_from")
	private Date validFrom;

	@Column(name = "valid_to")
	private Date validTo;

}
