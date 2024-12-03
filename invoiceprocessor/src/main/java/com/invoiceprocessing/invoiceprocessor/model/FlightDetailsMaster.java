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
@Table(name = "FLIGHT_DETAILS_MASTER")
public class FlightDetailsMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FLIGHT_DETAILS_MASTER_ID")
	private Integer flightDetailsMasterId;

	@Column(name = "FROM_LOCATION", length = 10)
	private String fromLocation;

	@Column(name = "TO_LOCATION", length = 10)
	private String toLocation;

	@Column(name = "DEPARTURE_DATE")
	private Date departureDate;

	@Column(name = "ARRIVAL_DATE")
	private Date arrivalDate;

	@Column(name = "FARE_TYPE", length = 25)
	private String fareType;

	@Column(name = "DEPARTURE_TIME", length = 25)
	private String departureTime;

	@Column(name = "ARRIVAL_TIME", length = 25)
	private String arrivalTime;

	@Column(name = "MERCHANT", length = 45)
	private String merchant;

	@Column(name = "PNR_NO", length = 45)
	private String pnrNumber;

}
