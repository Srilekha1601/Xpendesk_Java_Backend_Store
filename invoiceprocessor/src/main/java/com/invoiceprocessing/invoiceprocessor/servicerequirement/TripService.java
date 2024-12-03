package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;
import com.invoiceprocessing.invoiceprocessor.response.TravelTripCreationDto;
import com.invoiceprocessing.invoiceprocessor.response.TripDTO;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;

public interface TripService {

	public Trip saveAndGenerateTripID(TripDTO tripInfo);

	public TripDTO fetchEmployeeById(Integer tripId);

	public List<VoucherDto> getAllTripInfo(Integer tripId);

	public List<VoucherDto> getIndividualVoucherRecord(VoucherDto voucherInfo);

	public ResponseMassageWithCodeDto updateTravelDetails(TravelTripCreationDto travelTripCreationDto);

	public ResponseMassageWithCodeDto deleteTripById(VoucherDto voucherDto);

}
