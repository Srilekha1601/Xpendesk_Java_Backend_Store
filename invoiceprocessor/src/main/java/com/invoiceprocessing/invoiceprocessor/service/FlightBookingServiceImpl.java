package com.invoiceprocessing.invoiceprocessor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.mapper.FlightBookingMapperImpl;
import com.invoiceprocessing.invoiceprocessor.model.FlightBookingMaster;
import com.invoiceprocessing.invoiceprocessor.model.FlightDetailsMaster;
import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.repository.FlightBookingMasterRepository;
import com.invoiceprocessing.invoiceprocessor.repository.FlightDetailsMasterRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripRepository;
import com.invoiceprocessing.invoiceprocessor.response.FlightBookingMasterDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.FlightBookingService;
import com.invoiceprocessing.invoiceprocessor.utils.XpendeskUtils;

@Service
public class FlightBookingServiceImpl implements FlightBookingService {

	@Autowired
	private FlightDetailsMasterRepository flightDetailsMasterRepository;

	@Autowired
	TripRepository tripRepository;

	@Autowired
	FlightBookingMapperImpl flightBookingMapperImpl;

	@Autowired
	FlightBookingMasterRepository flightBookingMasterRepository;

	@Override
	public FlightBookingMasterDto bookFlight(FlightBookingMasterDto flightBookingMasterDto) {
		// TODO Auto-generated method stub

		Trip trip = tripRepository.findById(flightBookingMasterDto.getTripId()).get();

		FlightDetailsMaster flightDetailsMaster = flightDetailsMasterRepository
				.findById(Integer.parseInt(flightBookingMasterDto.getFlightDetailsMasterId())).get();

		FlightBookingMaster flightBookingMaster = new FlightBookingMaster();

		flightBookingMaster.setFromLocation(flightBookingMasterDto.getFromLocation());
		flightBookingMaster.setToLocation(flightBookingMasterDto.getToLocation());
		flightBookingMaster
				.setDepartureDate(XpendeskUtils.parseStringToDate(flightBookingMasterDto.getDepartureDate()));
		flightBookingMaster.setArrivalDate(XpendeskUtils.parseStringToDate(flightBookingMasterDto.getArrivalDate()));
		flightBookingMaster.setFareType(flightBookingMasterDto.getFareType());
		flightBookingMaster.setDepartureTime(flightBookingMasterDto.getDepartureTime());
		flightBookingMaster.setArrivalTime(flightBookingMasterDto.getArrivalTime());
		flightBookingMaster.setFlightDetailsMasterId(flightDetailsMaster);
		flightBookingMaster.setTrip(trip);
		flightBookingMaster.setPnrNumber(flightDetailsMaster.getPnrNumber());
		flightBookingMaster.setTicketClass("Economy");
		flightBookingMaster.setPaxCount("1");

		flightBookingMaster = flightBookingMasterRepository.save(flightBookingMaster);

		return flightBookingMapperImpl.mapBookingMasterToBookingMasterDto(flightBookingMaster);
	}
}