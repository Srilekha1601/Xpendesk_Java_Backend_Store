package com.invoiceprocessing.invoiceprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.FlightBookingMaster;

@Repository
public interface FlightBookingMasterRepository extends JpaRepository<FlightBookingMaster, Integer> {

}