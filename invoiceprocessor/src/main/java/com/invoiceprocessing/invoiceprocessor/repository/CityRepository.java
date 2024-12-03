package com.invoiceprocessing.invoiceprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

	@Query("SELECT p FROM City p WHERE p.cityName=?1")
	City findByCityCode(@Param("cityName") String hotelCity);

}
