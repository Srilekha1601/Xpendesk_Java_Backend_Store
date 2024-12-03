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
@Table(name = "CITY")
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CITY_ID")
	private Integer cityID;

	@Column(name = "CITY_NAME", nullable = false, length = 100)
	private String cityName;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "TIER_ID", nullable = false)
	private Tier tierID;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "STATE_ID", nullable = false)
	private State ststeID;

	public Integer getCityID() {
		return cityID;
	}

	public void setCityID(Integer cityID) {
		this.cityID = cityID;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Tier getTireID() {
		return tierID;
	}

	public void setTireID(Tier tireID) {
		this.tierID = tireID;
	}

	public State getStsteID() {
		return ststeID;
	}

	public void setStsteID(State ststeID) {
		this.ststeID = ststeID;
	}

}
