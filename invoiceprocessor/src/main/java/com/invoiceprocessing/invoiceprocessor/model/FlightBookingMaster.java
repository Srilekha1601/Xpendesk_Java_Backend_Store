package com.invoiceprocessing.invoiceprocessor.model;

import java.sql.Date;

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
@Table(name = "FLIGHT_BOOKING_MASTER")
public class FlightBookingMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FLIGHT_BOOKING_ID")
	private Integer flightBookingId;

	@ManyToOne
	@JoinColumn(name = "TRIP_ID", referencedColumnName = "TRIP_ID")
	private Trip trip;

	@Column(name = "FROM_LOCATION", length = 5)
	private String fromLocation;

	@Column(name = "TO_LOCATION", length = 5)
	private String toLocation;

	@Column(name = "DEPARTURE_DATE")
	private Date departureDate;

	@Column(name = "ARRIVAL_DATE")
	private Date arrivalDate;

	@Column(name = "DEPARTURE_TIME", length = 10)
	private String departureTime;

	@Column(name = "ARRIVAL_TIME", length = 10)
	private String arrivalTime;

	@Column(name = "PNR_NUMBER", length = 50)
	private String pnrNumber;

	@Column(name = "TICKET_CLASS", length = 2)
	private String ticketClass;

	@Column(name = "TRIP_TYPE", length = 1)
	private String tripType;

	@Column(name = "PAX_COUNT")
	private String paxCount;

	@Column(name = "FARE_TYPE", length = 2)
	private String fareType;

	@ManyToOne
	@JoinColumn(name = "FLIGHT_DETAILS_MASTER_ID", referencedColumnName = "FLIGHT_DETAILS_MASTER_ID")
	private FlightDetailsMaster flightDetailsMasterId;

}
