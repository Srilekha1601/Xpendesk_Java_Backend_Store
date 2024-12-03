package com.invoiceprocessing.invoiceprocessor.model;

import java.sql.Date;
import java.util.List;

import com.invoiceprocessing.invoiceprocessor.markerentity.TripEntity;
import com.invoiceprocessing.invoiceprocessor.service.XpendeskEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "TRIP")
@EntityListeners(XpendeskEntityListener.class)
public class Trip extends AuditFields implements TripEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRIP_ID")
	private Integer tripID;

	@Column(name = "FROM_DATE", nullable = false)
	private Date fromDate;

	@Column(name = "TO_DATE", nullable = false)
	private Date toDate;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "EMPLOYEE_ID")
	private Employee employeeID;

	@Column(name = "EXPENSE_STATUS", length = 1)
	private String expenseStatus;

	@OneToMany(mappedBy = "trip", fetch = FetchType.LAZY)
	private List<Voucher> vouchers;

	@Column(name = "TRIP_NAME", length = 255)
	private String tripName;

	@Column(name = "DELETE_STATUS", length = 1)
	private String deleteStatus;

	@PrePersist
	public void prePersist() {
		if (deleteStatus == null) {
			deleteStatus = "N";
		}
	}

	public Integer getTripID() {
		return tripID;
	}

	public void setTripID(Integer tripID) {
		this.tripID = tripID;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Employee getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(Employee employeeID) {
		this.employeeID = employeeID;
	}

	public String getExpenseStatus() {
		return expenseStatus;
	}

	public void setExpenseStatus(String expenseStatus) {
		this.expenseStatus = expenseStatus;
	}

	public List<Voucher> getVouchers() {
		return vouchers;
	}

	public void setVouchers(List<Voucher> vouchers) {
		this.vouchers = vouchers;
	}

	public String getTripName() {
		return tripName;
	}

	public void setTripName(String tripName) {
		this.tripName = tripName;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

}
