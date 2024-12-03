//package com.invoiceprocessing.invoiceprocessor.conversion;
//
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//import com.invoiceprocessing.invoiceprocessor.model.Voucher;
//import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
//
//@Mapper(componentModel = "spring")
//public abstract class WithOutTripVoucherMapper {
//	
//	@Mapping(source = "voucherDto.filename", target = "fileName")
//	@Mapping(source = "voucherDto.billType", target = "invoiceType")
//	@Mapping(source = "voucherDto.merchantName", target = "merchantName")
//	@Mapping(source = "voucherDto.image", target = "fileImage")
//	@Mapping(source = "voucherDto.invoiceNo", target = "invoiceNumber")
//	@Mapping(source = "voucherDto.totalAmount", target = "totalAmount")
//	@Mapping(source = "voucherDto.date", target = "invoiceDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
//	@Mapping(source = "voucherDto.manualEntry", target = "manualEntry")
//	@Mapping(source = "voucherDto.pax", target = "totalPax")
//	@Mapping(source = "voucherDto.date", target = "voucherDate")
//	@Mapping(source = "voucherDto.date", target = "voucherDate")
//	public abstract Voucher dtoToVoucherWithOuthTrip(VoucherDto voucherDto);
//}
