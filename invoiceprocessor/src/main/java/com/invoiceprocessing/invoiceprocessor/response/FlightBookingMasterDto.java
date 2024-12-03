package com.invoiceprocessing.invoiceprocessor.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class FlightBookingMasterDto {

	private Integer flightBookingId;

	private Integer tripId;

	private String fromLocation;

	private String toLocation;

	private String departureDate;

	private String arrivalDate;

	private String departureTime;

	private String arrivalTime;

	private String pnrNumber;

	private String ticketClass;

	private String tripType;

	private String paxCount;

	private String fareType;

	@JsonIgnore
	private String merchant;

	private String flightDetailsMasterId;

}
