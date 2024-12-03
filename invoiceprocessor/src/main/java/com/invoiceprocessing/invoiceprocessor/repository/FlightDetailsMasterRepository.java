package com.invoiceprocessing.invoiceprocessor.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.FlightDetailsMaster;

@Repository
public interface FlightDetailsMasterRepository extends JpaRepository<FlightDetailsMaster, Integer> {

	List<FlightDetailsMaster> findByDepartureDate(Date departureDate);

	List<FlightDetailsMaster> findByFromLocationAndToLocationAndDepartureDate(String fromLocation, String toLocation,
			Date departureDate);

}