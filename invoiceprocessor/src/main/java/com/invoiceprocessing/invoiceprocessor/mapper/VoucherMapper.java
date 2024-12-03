package com.invoiceprocessing.invoiceprocessor.mapper;

import com.invoiceprocessing.invoiceprocessor.model.VoucherEntity;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;

public interface VoucherMapper<S extends VoucherDto, T extends VoucherEntity> {
	
	public T dtoToModel(S source);
	public S modelToDto(T target);

}
