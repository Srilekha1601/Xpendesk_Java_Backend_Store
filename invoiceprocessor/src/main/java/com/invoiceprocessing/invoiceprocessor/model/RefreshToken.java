package com.invoiceprocessing.invoiceprocessor.model;

import java.sql.Time;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Builder
@Table(name = "AUTH_DETAILS")
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tokenID;

	@Column(name = "TOKEN", length = 300)
	private String token;

	@Column(name = "EXPIRY_DATE")
	private Instant expiryDate;

	@Column(name = "LOGIN_TIME")
	private Time loginTime;

	@Column(name = "LOGOUT_TIME")
	private Time logoutTime;

	@Column(name = "SIGNUP_TIME")
	private Time signupTime;

	@OneToOne
	@JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "EMPLOYEE_ID")
	private Employee employee;

	public Integer getTokenID() {
		return tokenID;
	}

	public void setTokenID(Integer tokenID) {
		this.tokenID = tokenID;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Instant getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Instant expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Time getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Time loginTime) {
		this.loginTime = loginTime;
	}

	public Time getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Time logoutTime) {
		this.logoutTime = logoutTime;
	}

	public Time getSignupTime() {
		return signupTime;
	}

	public void setSignupTime(Time signupTime) {
		this.signupTime = signupTime;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
