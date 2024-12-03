package com.invoiceprocessing.invoiceprocessor.model;

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
@Table(name = "PJP_HEADER")
public class PjpHeader {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pjp_id")
	private Integer pjpId;

	@ManyToOne
	@JoinColumn(name = "employee_id", referencedColumnName = "EMPLOYEE_ID")
	private Employee employeeId;

	@Column(name = "month")
	private Integer month;

	@Column(name = "year")
	private Integer year;

}
