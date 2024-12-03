package com.invoiceprocessing.invoiceprocessor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "AIRPORT_MASTER")
public class AirportDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AIRPORT_MASTER_ID")
	private Integer airportMasterId;

	@Column(name = "AIRPORT_NAME", length = 200)
	private String airportName;

	@Column(name = "AIRPORT_CODE", length = 5, unique = true, nullable = false)
	private String airportCode;

	@Column(name = "CITY", length = 20)
	private String city;

	@Column(name = "STATE", length = 20)
	private String state;

	@Column(name = "COUNTRY", length = 25)
	private String country;

	@Column(name = "AIRPORT_STATUS", length = 1)
	private String airportStatus;

}