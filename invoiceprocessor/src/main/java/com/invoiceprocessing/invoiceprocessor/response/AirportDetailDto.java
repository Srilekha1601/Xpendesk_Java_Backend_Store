package com.invoiceprocessing.invoiceprocessor.response;

import lombok.Data;

@Data
public class AirportDetailDto {

	private Integer airportMasterId;

	private String airportName;

	private String airportCode;

	private String city;

	private String state;

	private String country;

	private String airportStatus;

}
