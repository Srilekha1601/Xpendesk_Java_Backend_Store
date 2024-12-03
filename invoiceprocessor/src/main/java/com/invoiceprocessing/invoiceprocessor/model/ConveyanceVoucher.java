package com.invoiceprocessing.invoiceprocessor.model;

import java.math.BigDecimal;
import java.sql.Date;
import com.invoiceprocessing.invoiceprocessor.service.XpendeskEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONVEYANCE_VOUCHER")
@EntityListeners(XpendeskEntityListener.class)
public class ConveyanceVoucher extends AuditFields implements VoucherEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CONVEYANCE_VOUCHER_ID")
	private Integer conveyanceVoucherID;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "VOUCHER_ID")
	private Voucher voucherID;

	@Column(name = "MODE_OF_TRAVEL", length = 20)
	private String modeOfTravel;

	@Column(name = "CAB_TYPE", length = 50)
	private String cabType;

	@Column(name = "TRAVEL_CLASS", length = 20)
	private String travelClass;

	@Column(name = "VEHICLE_TYPE", length = 1)
	private String vehicleType;

	@Column(name = "INTRA_INTER_CITY", length = 10)
	private String interIntraCity;

	@Column(name = "FROM_LOCATION", length = 100)
	private String fromLocation;

	@Column(name = "TO_LOCATION", length = 100)
	private String toLocation;

	@Column(name = "DEPARTURE_DATE")
	private Date departureDate;

	@Column(name = "DEPARTURE_TIME")
	private String departureTime;

	@Column(name = "ARRIVAL_DATE")
	private Date arrivalDate;

	@Column(name = "ARRIVAL_TIME")
	private String arrivalTime;

	@Column(name = "DISTANCE", precision = 10, scale = 2)
	private BigDecimal distance;

	@Column(name = "VEHICLE_FUEL_TYPE", length = 50)
	private String vehicleFuelType;

	public Integer getConveyanceVoucherID() {
		return conveyanceVoucherID;
	}

	public void setConveyanceVoucherID(Integer conveyanceVoucherID) {
		this.conveyanceVoucherID = conveyanceVoucherID;
	}

	public Voucher getVoucherID() {
		return voucherID;
	}

	public void setVoucherID(Voucher voucherID) {
		this.voucherID = voucherID;
	}

	public String getModeOfTravel() {
		return modeOfTravel;
	}

	public void setModeOfTravel(String modeOfTravel) {
		this.modeOfTravel = modeOfTravel;
	}

	public String getTravelClass() {
		return travelClass;
	}

	public void setTravelClass(String travelClass) {
		this.travelClass = travelClass;
	}

	public String getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(String fromLocation) {
		this.fromLocation = fromLocation;
	}

	public String getToLocation() {
		return toLocation;
	}

	public void setToLocation(String toLocation) {
		this.toLocation = toLocation;
	}

	public BigDecimal getDistance() {
		return distance;
	}

	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

	public String getInterIntraCity() {
		return interIntraCity;
	}

	public void setInterIntraCity(String interIntraCity) {
		this.interIntraCity = interIntraCity;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getCabType() {
		return cabType;
	}

	public void setCabType(String cabType) {
		this.cabType = cabType;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehicleFuelType() {
		return vehicleFuelType;
	}

	public void setVehicleFuelType(String vehicleFuelType) {
		this.vehicleFuelType = vehicleFuelType;
	}

}
