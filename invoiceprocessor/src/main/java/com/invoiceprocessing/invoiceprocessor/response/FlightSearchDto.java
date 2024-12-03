package com.invoiceprocessing.invoiceprocessor.response;

import lombok.Data;

@Data
public class FlightSearchDto {

	private Integer flightDetailsMasterId;

	private String fromLocation;

	private String toLocation;

	private String departureDate;

	private String arrivalDate;

	private String fareType;

	private String departureTime;

	private String arrivalTime;

	private String merchant;

	private String pnrNumber;

}
