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
@Table(name = "OFFICE")
public class Office {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "OFFICE_ID")
	private Integer officeID;

	@Column(name = "ADDRESS", nullable = false, length = 100)
	private String address;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "CITY_ID", nullable = false)
	private City cityID;

	@Column(name = "PIN_CODE", nullable = false)
	private Integer pinCode;

	public Integer getOfficeID() {
		return officeID;
	}

	public void setOfficeID(Integer officeID) {
		this.officeID = officeID;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public City getCityID() {
		return cityID;
	}

	public void setCityID(City cityID) {
		this.cityID = cityID;
	}

	public Integer getPinCode() {
		return pinCode;
	}

	public void setPinCode(Integer pinCode) {
		this.pinCode = pinCode;
	}

}
