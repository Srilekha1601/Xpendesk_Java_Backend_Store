package com.invoiceprocessing.invoiceprocessor.mapper;

import com.invoiceprocessing.invoiceprocessor.markerentity.TripEntity;
import com.invoiceprocessing.invoiceprocessor.response.TripDTO;

public interface TripMapper<S extends TripDTO, T extends TripEntity> {

	public T dtoToModel(S source);
	public S modelToDto(T target);
	
}
