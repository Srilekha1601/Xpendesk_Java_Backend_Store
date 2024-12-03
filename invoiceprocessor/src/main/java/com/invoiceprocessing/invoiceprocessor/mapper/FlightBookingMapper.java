package com.invoiceprocessing.invoiceprocessor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.invoiceprocessing.invoiceprocessor.model.FlightBookingMaster;
import com.invoiceprocessing.invoiceprocessor.response.FlightBookingMasterDto;

@Mapper(componentModel = "spring")
public abstract class FlightBookingMapper {

	@Mapping(target = "flightDetailsMasterId", source = "flightBookingMaster", qualifiedByName = "getFlightDetailsMasterId")
	@Mapping(target = "tripId", source = "flightBookingMaster", qualifiedByName = "getTripidWithTrip")
	@Mapping(target = "merchant", ignore = true)
	public abstract FlightBookingMasterDto mapBookingMasterToBookingMasterDto(FlightBookingMaster flightBookingMaster);

	@Named("getFlightDetailsMasterId")
	protected String getFlightDetailsMasterId(FlightBookingMaster flightBookingMaster) {
		return flightBookingMaster.getFlightDetailsMasterId().getFlightDetailsMasterId().toString();
	}

	@Named("getTripidWithTrip")
	protected Integer getTripidWithTrip(FlightBookingMaster flightBookingMaster) {
		return flightBookingMaster.getTrip().getTripID();
	}

}
