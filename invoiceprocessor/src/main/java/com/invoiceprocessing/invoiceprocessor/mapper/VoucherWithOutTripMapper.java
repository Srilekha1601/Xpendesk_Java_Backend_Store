package com.invoiceprocessing.invoiceprocessor.mapper;

import com.invoiceprocessing.invoiceprocessor.model.VoucherEntity;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;

public interface VoucherWithOutTripMapper<S extends VoucherWithOutTripDto, T extends VoucherEntity> {

	T dtoToModelWithOutTrip(S source);

	S modelToDtoWithOutTrip(T target);

}
