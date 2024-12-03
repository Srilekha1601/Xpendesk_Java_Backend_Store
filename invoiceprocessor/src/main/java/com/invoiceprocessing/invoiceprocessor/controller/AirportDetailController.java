package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.AirportDetailDto;
import com.invoiceprocessing.invoiceprocessor.response.FlightBookingMasterDto;
import com.invoiceprocessing.invoiceprocessor.response.FlightSearchDto;
import com.invoiceprocessing.invoiceprocessor.service.AirportServiceImpl;
import com.invoiceprocessing.invoiceprocessor.service.FlightBookingServiceImpl;

@RestController
@RequestMapping("/airport-detail")
public class AirportDetailController {

	@Autowired
	AirportServiceImpl airportServiceImpl;

	@Autowired
	FlightBookingServiceImpl flightBookingServiceImpl;

	@GetMapping("/get-airport-detail")
	public ResponseEntity<List<AirportDetailDto>> getAirportDetail() {
		return new ResponseEntity<List<AirportDetailDto>>(airportServiceImpl.getAllDetailOfAirport(),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

	@PostMapping("/get-all-flight-info")
	public ResponseEntity<List<FlightSearchDto>> getFlightInfo(@RequestBody FlightSearchDto flightSearchDto) {
		return new ResponseEntity<List<FlightSearchDto>>(airportServiceImpl.getAllSearchFlightInfo(flightSearchDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

	@PostMapping("/booked-flight")
	public ResponseEntity<FlightBookingMasterDto> bookedFlightInfo(
			@RequestBody FlightBookingMasterDto flightBookingMasterDto) {
		return new ResponseEntity<FlightBookingMasterDto>(flightBookingServiceImpl.bookFlight(flightBookingMasterDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

}
