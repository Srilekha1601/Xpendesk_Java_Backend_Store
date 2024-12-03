package com.invoiceprocessing.invoiceprocessor.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.invoiceprocessing.invoiceprocessor.model.FlightDetailsMaster;
import com.invoiceprocessing.invoiceprocessor.response.FlightSearchDto;

@Mapper(componentModel = "spring")
public abstract class FlightInfoSearchMapper {

	public abstract List<FlightSearchDto> mapFlightDetailsListToFlightInfoSearchDtoList(
			List<FlightDetailsMaster> listOfFlightDetailsMasters);

	public abstract FlightSearchDto mapFlightDetailsToDto(FlightDetailsMaster flightDetailsMaster);

}
