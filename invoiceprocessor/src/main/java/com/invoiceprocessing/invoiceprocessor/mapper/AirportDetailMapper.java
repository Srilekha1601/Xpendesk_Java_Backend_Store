package com.invoiceprocessing.invoiceprocessor.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.invoiceprocessing.invoiceprocessor.model.AirportDetail;
import com.invoiceprocessing.invoiceprocessor.response.AirportDetailDto;

@Mapper(componentModel = "spring")
public abstract class AirportDetailMapper {

	public abstract List<AirportDetailDto> mapListAirportToListDto(List<AirportDetail> listAirportDetails);

	public abstract AirportDetailDto mapAirportToDto(AirportDetail airportDetail);

}
