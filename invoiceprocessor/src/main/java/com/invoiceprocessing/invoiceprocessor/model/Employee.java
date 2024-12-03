package com.invoiceprocessing.invoiceprocessor.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMPLOYEE_ID")
	private Integer employeeId;

	@Column(name = "EMPLOYEE_CODE", nullable = false, length = 25)
	private String employeeCode;

	@Column(name = "USER_NAME", nullable = false, length = 25)
	private String userName;

	@Column(name = "EMPLOYEE_NAME", nullable = false, length = 100)
	private String name;

	@Column(name = "PASSWORD", length = 100)
	private String password;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "OFFICE_ID", nullable = false)
	private Office officeID;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "CITY_ID", nullable = false)
	private City cityID;

	@Column(name = "PIN_CODE", nullable = false)
	private Integer pinCode;

	@Column(name = "EMAIL_ID", length = 100)
	private String emailID;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "PARTNER_ID", nullable = false)
	private Employee partner;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ASSISTANT_ID", nullable = false)
	private Employee assistantId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "GRADE_ID", nullable = false)
	private Grade gradeID;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "MANAGER_ID", nullable = false)
	private Employee manager;

	@OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
	private List<EmployeeRole> employeeRoleList;

}
