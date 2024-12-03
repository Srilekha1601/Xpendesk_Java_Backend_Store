package com.invoiceprocessing.invoiceprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.TripCities;

@Repository
public interface TripCitiesRepository extends JpaRepository<TripCities, Integer>{

}
