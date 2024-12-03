package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;
import com.invoiceprocessing.invoiceprocessor.response.TravelTripCreationDto;
//import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.response.TripDTO;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.service.TripServiceImpl;

@RestController
@RequestMapping("/trip")
public class TripController {

	@Autowired
	TripServiceImpl tripServiceImp;

	@PostMapping("/tripdetails")
	public ResponseEntity<Map<String, String>> generateTripID(@RequestBody TripDTO tripDTO) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("TripId", tripServiceImp.saveAndGenerateTripID(tripDTO).getTripID().toString());
		return ResponseEntity.ok(map);
	}

	@GetMapping("/getnotsavedtrip/{tripId}")
	public ResponseEntity<TripDTO> getTrip(@PathVariable Integer tripId) {
		return ResponseEntity.ok(tripServiceImp.fetchEmployeeById(tripId));
	}

	@GetMapping("/gettripinfo/{tripId}")
	public ResponseEntity<List<VoucherDto>> getAllTripDetails(@PathVariable Integer tripId) {
		return ResponseEntity.ok(tripServiceImp.getAllTripInfo(tripId));
	}

	@PostMapping("/individual-voucher-info")
	public ResponseEntity<List<VoucherDto>> getIndividualVoucherDetails(@RequestBody VoucherDto voucherInfo) {
		return ResponseEntity.ok(tripServiceImp.getIndividualVoucherRecord(voucherInfo));
	}

	@PostMapping("/travel-desk")
	public ResponseEntity<ResponseMassageWithCodeDto> doUpdateTripAndSaveProjectAndCostCode(
			@RequestBody TravelTripCreationDto travelTripCreationDto) {
		return new ResponseEntity<ResponseMassageWithCodeDto>(tripServiceImp.updateTravelDetails(travelTripCreationDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

	@PostMapping("/delete-trip-by-id")
	public ResponseEntity<ResponseMassageWithCodeDto> deleteTripById(@RequestBody VoucherDto voucherDto) {
		return new ResponseEntity<ResponseMassageWithCodeDto>(tripServiceImp.deleteTripById(voucherDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

}