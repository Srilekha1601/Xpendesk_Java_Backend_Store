package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import com.invoiceprocessing.invoiceprocessor.response.FlightBookingMasterDto;

public interface FlightBookingService {

	public FlightBookingMasterDto bookFlight(FlightBookingMasterDto flightBookingMasterDto);

}