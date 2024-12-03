package com.invoiceprocessing.invoiceprocessor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "EMPLOYEE_ROLE_CROSS_REF")
public class EmployeeRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMPLOYEE_ROLE_ID")
	private Integer employeeRollID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EMPLOYEE_ID")
	private Employee employee;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ROLE_ID")
	private Role roleID;

	public Integer getEmployeeRollID() {
		return employeeRollID;
	}

	public void setEmployeeRollID(Integer employeeRollID) {
		this.employeeRollID = employeeRollID;
	}

	public Employee getEmpliyeeID() {
		return employee;
	}

	public void setEmpliyeeID(Employee employeeID) {
		this.employee = employeeID;
	}

	public Role getRoleID() {
		return roleID;
	}

	public void setRoleID(Role roleID) {
		this.roleID = roleID;
	}

}
