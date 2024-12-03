package com.invoiceprocessing.invoiceprocessor.mapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.invoiceprocessing.invoiceprocessor.model.ConveyanceVoucher;
import com.invoiceprocessing.invoiceprocessor.model.FoodVoucher;
import com.invoiceprocessing.invoiceprocessor.model.HotelVoucher;
import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.model.VoucherEntity;
import com.invoiceprocessing.invoiceprocessor.repository.ConveyanceVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.FoodVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.HotelVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherRepository;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants;

@Configuration("manual")
public class ManualEntryMapper implements VoucherMapper<VoucherDto, VoucherEntity> {

	@Autowired
	TripRepository tripRepository;

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	ConveyanceVoucherRepository conveyanceVoucherRepository;

	@Autowired
	HotelVoucherRepository hotelVoucherRepository;

	@Autowired
	FoodVoucherRepository foodVoucherRepository;

	@Override
	public VoucherEntity dtoToModel(VoucherDto manualVoucher) {
		Voucher voucher = new Voucher();
		ConveyanceVoucher conveyanceVoucher = new ConveyanceVoucher();
		HotelVoucher hotelVoucher = new HotelVoucher();
		FoodVoucher foodVoucher = new FoodVoucher();
		insertDataInEntity(manualVoucher, voucher, conveyanceVoucher, hotelVoucher, foodVoucher);

		voucher = voucherRepository.save(voucher);

		if (manualVoucher.getBillType().equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)) {
			conveyanceVoucher.setVoucherID(voucher);
			conveyanceVoucher = conveyanceVoucherRepository.save(conveyanceVoucher);
			voucher.setConveyanceVoucherID(conveyanceVoucher);
		} else if (manualVoucher.getBillType().equalsIgnoreCase(XpendeskConstants.ACCOMMODATION_TYPE)) {
			hotelVoucher.setVoucherID(voucher);
			hotelVoucher = hotelVoucherRepository.save(hotelVoucher);
			voucher.setHotelVoucherID(hotelVoucher);
		} else if (manualVoucher.getBillType().equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)) {
			foodVoucher.setVoucherID(voucher);
			foodVoucher = foodVoucherRepository.save(foodVoucher);
			voucher.setFoodVoucherID(foodVoucher);
		}
		return voucher;
//		return voucherRepository.save(voucher);
	}

	@Override
	public VoucherDto modelToDto(VoucherEntity target) {

		return null;
	}

	private Trip getTripByTripId(Integer tripId) {
		Optional<Trip> tripEntity = tripRepository.findById(tripId);
		return tripEntity.get();
	}

	private static Date parseStringToDate(String date) {
		Date outDate = null;
		try {
			outDate = new java.sql.Date(((java.util.Date) new SimpleDateFormat("dd-MM-yyyy").parse(date)).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return outDate;
	}

	private void insertDataInEntity(VoucherDto manualVoucher, Voucher voucher, ConveyanceVoucher conveyanceVoucher,
			HotelVoucher hotelVoucher, FoodVoucher foodVoucher) {
		voucher.setInvoiceType(manualVoucher.getBillType());
		voucher.setTotalAmount(new BigDecimal(manualVoucher.getTotalAmount().equalsIgnoreCase("Not Found") ? "0.0"
				: manualVoucher.getTotalAmount().replace(",", "")));
		Long utilDate = new java.util.Date().getTime();
		voucher.setVoucherDate(
				manualVoucher.getDate().replace("/", " ").equalsIgnoreCase("Not Found") ? new java.sql.Date(utilDate)
						: parseStringToDate(manualVoucher.getDate()));
		voucher.setInvoiceDate(parseStringToDate(manualVoucher.getDate()));
		if (!manualVoucher.getTripId().equalsIgnoreCase(""))
			voucher.setTrip(getTripByTripId(Integer.parseInt(manualVoucher.getTripId())));
		voucher.setManualEntry(manualVoucher.getManualEntry());
		if (manualVoucher.getVoucherID() != null && !manualVoucher.getVoucherID().isEmpty())
			voucher.setVoucherID(Integer.parseInt(manualVoucher.getVoucherID()));
		voucher.setDeleteStatus(
				manualVoucher.getDeleteStatus() == null || manualVoucher.getDeleteStatus().equalsIgnoreCase("")
						|| manualVoucher.getDeleteStatus().equalsIgnoreCase("N") ? "N"
								: manualVoucher.getDeleteStatus());
		if (!manualVoucher.getTripId().equalsIgnoreCase(""))
			voucher.setEmployeeID(getTripByTripId(Integer.parseInt(manualVoucher.getTripId())).getEmployeeID());
		voucher.setClaimedAmount(new BigDecimal(manualVoucher.getClaimedAmount() == null
				? manualVoucher.getTotalAmount().equalsIgnoreCase("Not Found") ? "0.0" : manualVoucher.getTotalAmount()
				: manualVoucher.getClaimedAmount()));
		if (manualVoucher.getBillType().equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)) {
			if (manualVoucher.getConveyanceVoucherID() != null && !manualVoucher.getConveyanceVoucherID().isEmpty())
				conveyanceVoucher.setConveyanceVoucherID(Integer.parseInt(manualVoucher.getConveyanceVoucherID()));
			conveyanceVoucher.setCabType(manualVoucher.getCabType());
			conveyanceVoucher.setDistance(manualVoucher.getNoOfKm().equalsIgnoreCase("") ? new BigDecimal("0.0")
					: new BigDecimal(manualVoucher.getNoOfKm()));
			conveyanceVoucher.setModeOfTravel(manualVoucher.getModeOfTravel());
			conveyanceVoucher.setVehicleType(
					!manualVoucher.getVehicleType().equalsIgnoreCase("") ? manualVoucher.getVehicleType() : "");
			conveyanceVoucher.setTravelClass(
					!manualVoucher.getTravelClass().equalsIgnoreCase("") ? manualVoucher.getTravelClass() : "");
			conveyanceVoucher.setVehicleFuelType(
					manualVoucher.getVehicleFuelType().isEmpty() ? null : manualVoucher.getVehicleFuelType());
//			conveyanceVoucher.setInterIntraCity(
//					!manualVoucher.getInterIntraCity().equalsIgnoreCase("") ? manualVoucher.getInterIntraCity() : "");
			conveyanceVoucher.setInterIntraCity(!manualVoucher.getIntraOrInterCityTravel().equalsIgnoreCase("")
					? manualVoucher.getIntraOrInterCityTravel()
					: "");
			conveyanceVoucher.setFromLocation(
					!manualVoucher.getFromLocation().equalsIgnoreCase("") ? manualVoucher.getFromLocation() : "");
			conveyanceVoucher.setToLocation(
					!manualVoucher.getToLocation().equalsIgnoreCase("") ? manualVoucher.getToLocation() : "");
		} else if (manualVoucher.getBillType().equalsIgnoreCase(XpendeskConstants.ACCOMMODATION_TYPE)) {
			if (manualVoucher.getHotelVoucherID() != null && !manualVoucher.getHotelVoucherID().isEmpty())
				hotelVoucher.setHotelVoucherID(Integer.parseInt(manualVoucher.getHotelVoucherID()));
			hotelVoucher.setAccomodationCategory(manualVoucher.getAccommodationType());
		} else if (manualVoucher.getBillType().equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)) {
			if (manualVoucher.getFoodVoucherID() != null && !manualVoucher.getFoodVoucherID().isEmpty())
				foodVoucher.setFoodVoucherID(Integer.parseInt(manualVoucher.getFoodVoucherID()));
			foodVoucher.setClaimedAmount(new BigDecimal(manualVoucher.getTotalAmount()));
			foodVoucher.setTotalAmount(new BigDecimal(manualVoucher.getTotalAmount()));
			foodVoucher.setCgstAmount("0.0");
			foodVoucher.setSgstAmount("0.0");
			foodVoucher.setGstAmount(new BigDecimal("0.0"));
			foodVoucher.setContainsLiquor(false);
		}
	}

}
