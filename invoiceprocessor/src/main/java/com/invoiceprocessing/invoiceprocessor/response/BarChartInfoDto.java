package com.invoiceprocessing.invoiceprocessor.response;

import lombok.Data;

@Data
public class BarChartInfoDto {

	private Integer totalFoodInvoices;

	private Integer totalHotelInvoices;

	private Integer totalConveyanceInvoices;

}
