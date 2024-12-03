package com.invoiceprocessing.invoiceprocessor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.mapper.AirportDetailMapperImpl;
import com.invoiceprocessing.invoiceprocessor.mapper.FlightInfoSearchMapperImpl;
import com.invoiceprocessing.invoiceprocessor.model.AirportDetail;
import com.invoiceprocessing.invoiceprocessor.model.FlightDetailsMaster;
import com.invoiceprocessing.invoiceprocessor.repository.AirportDetailRepository;
import com.invoiceprocessing.invoiceprocessor.repository.FlightDetailsMasterRepository;
import com.invoiceprocessing.invoiceprocessor.response.AirportDetailDto;
import com.invoiceprocessing.invoiceprocessor.response.FlightSearchDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.AirportService;
import com.invoiceprocessing.invoiceprocessor.utils.XpendeskUtils;

@Service
public class AirportServiceImpl implements AirportService {

	@Autowired
	AirportDetailRepository airportDetailRepository;

	@Autowired
	AirportDetailMapperImpl airportDetailMapperImpl;

	@Autowired
	FlightDetailsMasterRepository flightDetailsMasterRepository;

	@Autowired
	FlightInfoSearchMapperImpl flightInfoSearchMapperImpl;

	@Override
	public List<AirportDetailDto> getAllDetailOfAirport() {
		// TODO Auto-generated method stub
		List<AirportDetail> listAirportDetails = airportDetailRepository.findAll();
		return airportDetailMapperImpl.mapListAirportToListDto(listAirportDetails);
	}

	@Override
	public List<FlightSearchDto> getAllSearchFlightInfo(FlightSearchDto flightSearchDto) {
		// TODO Auto-generated method stub
		List<FlightDetailsMaster> listOfFlightDetailsMasters = flightDetailsMasterRepository
				.findByFromLocationAndToLocationAndDepartureDate(flightSearchDto.getFromLocation(),
						flightSearchDto.getToLocation(),
						XpendeskUtils.parseStringToDate(flightSearchDto.getDepartureDate()));
		return flightInfoSearchMapperImpl.mapFlightDetailsListToFlightInfoSearchDtoList(listOfFlightDetailsMasters);
	}

}
