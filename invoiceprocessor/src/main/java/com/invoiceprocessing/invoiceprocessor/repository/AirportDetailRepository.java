package com.invoiceprocessing.invoiceprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.AirportDetail;

@Repository
public interface AirportDetailRepository extends JpaRepository<AirportDetail, Integer> {

}