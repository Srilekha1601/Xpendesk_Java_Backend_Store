package com.invoiceprocessing.invoiceprocessor.model;

import java.sql.Date;
import java.util.List;

import com.invoiceprocessing.invoiceprocessor.service.XpendeskEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "HOTEL_VOUCHER")
@EntityListeners(XpendeskEntityListener.class)
public class HotelVoucher extends AuditFields implements VoucherEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "HOTEL_VOUCHER_ID")
	private Integer hotelVoucherID;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "VOUCHER_ID")
	private Voucher voucherID;

	@Column(name = "CHECK_IN_DATE")
	private Date checkInDate;

	@Column(name = "CHECK_OUT_DATE")
	private Date checkOutDate;

	@Column(name = "ACCOMODATION_CATEGORY", length = 50)
	private String accomodationCategory;

	@Column(name = "GUEST_NAME", length = 100)
	private String guestName;

	@Column(name = "GUEST_COMPANY_NAME", length = 100)
	private String guestCompanyName;

	@Column(name = "GUEST_COMPANY_GST_NO", length = 20)
	private String guestCompanyGstNumber;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CITY_ID")
	private City cityID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STATE_ID")
	private State ststeID;

	@Column(name = "PINCODE", length = 20)
	private String pinCode;

	@OneToMany(mappedBy = "hotelVoucherID", fetch = FetchType.LAZY)
	private List<HotelVoucherDetail> listOfHotelVoucherDetail;

	@OneToMany(mappedBy = "hotelVoucher", fetch = FetchType.LAZY)
	private List<FoodVoucherDetail> listOfFoodVoucherDetail;

	public Integer getHotelVoucherID() {
		return hotelVoucherID;
	}

	public void setHotelVoucherID(Integer hotelVoucherID) {
		this.hotelVoucherID = hotelVoucherID;
	}

	public Voucher getVoucherID() {
		return voucherID;
	}

	public void setVoucherID(Voucher voucherID) {
		this.voucherID = voucherID;
	}

	public Date getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}

	public Date getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public String getGuestCompanyName() {
		return guestCompanyName;
	}

	public void setGuestCompanyName(String guestCompanyName) {
		this.guestCompanyName = guestCompanyName;
	}

	public String getGuestCompanyGstNumber() {
		return guestCompanyGstNumber;
	}

	public void setGuestCompanyGstNumber(String guestCompanyGstNumber) {
		this.guestCompanyGstNumber = guestCompanyGstNumber;
	}

	public City getCityID() {
		return cityID;
	}

	public void setCityID(City cityID) {
		this.cityID = cityID;
	}

	public State getStsteID() {
		return ststeID;
	}

	public void setStsteID(State ststeID) {
		this.ststeID = ststeID;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public List<HotelVoucherDetail> getListOfHotelVoucherDetail() {
		return listOfHotelVoucherDetail;
	}

	public void setListOfHotelVoucherDetail(List<HotelVoucherDetail> listOfHotelVoucherDetail) {
		this.listOfHotelVoucherDetail = listOfHotelVoucherDetail;
	}

	public List<FoodVoucherDetail> getListOfFoodVoucherDetail() {
		return listOfFoodVoucherDetail;
	}

	public void setListOfFoodVoucherDetail(List<FoodVoucherDetail> listOfFoodVoucherDetail) {
		this.listOfFoodVoucherDetail = listOfFoodVoucherDetail;
	}

	public String getAccomodationCategory() {
		return accomodationCategory;
	}

	public void setAccomodationCategory(String accomodationCategory) {
		this.accomodationCategory = accomodationCategory;
	}

}
