package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.response.AirportDetailDto;
import com.invoiceprocessing.invoiceprocessor.response.FlightSearchDto;

public interface AirportService {

	public List<AirportDetailDto> getAllDetailOfAirport();

	public List<FlightSearchDto> getAllSearchFlightInfo(FlightSearchDto flightSearchDto);

	
}
