package com.invoiceprocessing.invoiceprocessor.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateMapper {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	public static Date stringToDate(String dateStr) throws ParseException {
		return dateFormat.parse(dateStr);
	}

	public static String dateToString(Date date) {
		return dateFormat.format(date);
	}
}
