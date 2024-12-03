package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.mapper.ConsolidatedHotelServiceBreakageMapper;
//import com.invoiceprocessing.invoiceprocessor.model.ConveyanceVoucher;
//import com.invoiceprocessing.invoiceprocessor.model.FoodVoucherDetail;
import com.invoiceprocessing.invoiceprocessor.model.HotelVoucher;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDetailDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.VoucherService;

@CrossOrigin(origins = "http://192.168.1.142:3000, http://192.168.1.143:3000, http://192.168.1.141:3000", allowCredentials = "true")
@RestController
@RequestMapping("/vouchers")
public class VoucherController {

	@Autowired
	VoucherService voucherServicsImp;

	@Autowired
	ConsolidatedHotelServiceBreakageMapper consolidatedHotelServiceBreakageMapper;

	@PostMapping("/hotelvoucher")
	public ResponseEntity<Map<String, Object>> hotelVoucherController(@RequestBody VoucherDto voucherDto) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		List<VoucherDetailDto> listOfVoucherDetailDtos = new ArrayList<VoucherDetailDto>();
//		List<String> listOfHotelVoucherDetailsId = new ArrayList<String>();
		HotelVoucher hotelVoucher = (HotelVoucher) voucherServicsImp.saveHotelVoucher(voucherDto);
		map.put("status", "Submit Successfully");
		map.put("voucher_id", hotelVoucher.getVoucherID().getVoucherID().toString());
		map.put("hotel_voucher_id", hotelVoucher.getHotelVoucherID().toString());
		hotelVoucher.getListOfHotelVoucherDetail().forEach(hotelVoucherDetail -> {
			listOfVoucherDetailDtos.add(
					consolidatedHotelServiceBreakageMapper.convertHotelVoucherDetailModelToDto(hotelVoucherDetail));
		});
		hotelVoucher.getListOfFoodVoucherDetail().forEach(foodVoucherDetail -> {
			listOfVoucherDetailDtos
					.add(consolidatedHotelServiceBreakageMapper.convertFoodVoucherDetailModelToDto(foodVoucherDetail));
		});

		map.put("consolidated_hotel_service_breakage", listOfVoucherDetailDtos);
		return ResponseEntity.ok(map);
//		return ResponseEntity.ok(voucherServicsImp.saveHotelVoucher(voucherDto));
	}

	@PostMapping("/foodvoucher")
	public ResponseEntity<Map<String, String>> foodVoucherController(@RequestBody VoucherDto voucherDto) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		Voucher voucher = (Voucher) voucherServicsImp.saveFoodVoucher(voucherDto);
		map.put("status", "Submit Successfully");
		map.put("food_voucher_id", voucher.getFoodVoucherID().getFoodVoucherID().toString());
		map.put("voucher_id", voucher.getVoucherID().toString());
		return ResponseEntity.ok(map);
	}

	@PostMapping("/conveyance")
	private ResponseEntity<Map<String, String>> conveyanceVoucherController(@RequestBody VoucherDto voucherDto) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		Voucher voucher = (Voucher) voucherServicsImp.saveFoodVoucher(voucherDto);
		map.put("status", "Submit Successfully");
		map.put("voucher_id", voucher.getVoucherID().toString());
		map.put("conveyance_voucher_id", voucher.getConveyanceVoucherID().getConveyanceVoucherID().toString());
		return ResponseEntity.ok(map);
	}

	@PostMapping("/get-voucher-info")
	public ResponseEntity<List<VoucherWithOutTripDto>> getVoucherInfoById(
			@RequestBody VoucherWithOutTripDto voucherWithOutTripDto) {
		return new ResponseEntity<List<VoucherWithOutTripDto>>(
				voucherServicsImp.getVoucherInfoByVoucherId(voucherWithOutTripDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

	@PostMapping("/delete-voucher-by-id")
	public ResponseEntity<ResponseMassageWithCodeDto> deleteVoucherByVoucherId(
			@RequestBody List<VoucherDto> listVoucherDtos) {
		return new ResponseEntity<ResponseMassageWithCodeDto>(
				voucherServicsImp.deleteVoucherWithVoucherId(listVoucherDtos),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

	@PostMapping("/delete-individual-voucher")
	public ResponseEntity<Map<String, Object>> deleteIndividualVoucher(@RequestBody VoucherDto voucherDto) {
		return new ResponseEntity<Map<String, Object>>(voucherServicsImp.deleteIndividualVoucher(voucherDto),
				HttpStatusCode.valueOf(HttpStatus.SC_OK));
	}

}
