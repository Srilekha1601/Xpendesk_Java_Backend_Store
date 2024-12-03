package com.invoiceprocessing.invoiceprocessor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TRIP_CITIES")
public class TripCities {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRIP_CITY_ID")
	private Integer tripCityID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TRIP_ID")
	private Trip tripID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CITY_ID")
	private City city;

	public Integer getTripCityID() {
		return tripCityID;
	}

	public void setTripCityID(Integer tripCityID) {
		this.tripCityID = tripCityID;
	}

	public Trip getTripID() {
		return tripID;
	}

	public void setTripID(Trip tripID) {
		this.tripID = tripID;
	}

	public City getCityID() {
		return city;
	}

	public void setCityID(City cityID) {
		this.city = cityID;
	}

}
