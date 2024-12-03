package com.invoiceprocessing.invoiceprocessor.response;

import java.util.List;

import lombok.Data;

@Data
public class TravelTripCreationDto {

	private Integer tripId;

	private List<String> projectCodes;

	private List<String> costCodes;

}
