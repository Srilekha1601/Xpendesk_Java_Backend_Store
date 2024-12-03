package com.invoiceprocessing.invoiceprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.invoiceprocessing.invoiceprocessor.model.TripApprovedAmount;

public interface TripApprovedAmountRepository extends JpaRepository<TripApprovedAmount, Integer>{

}
