package com.invoiceprocessing.invoiceprocessor.utils;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.invoiceprocessing.invoiceprocessor.securityconfiguration.XpendeskUserDetails;

public class XpendeskUtils {

	public static Integer extractLoggedInUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			XpendeskUserDetails userDetails = (XpendeskUserDetails) authentication.getPrincipal();
			return userDetails.getEmployeeId();
		}

		return -1;
	}

	public static java.sql.Date parseStringToDate(String date) {
		java.sql.Date outDate = null;
		try {
			java.util.Date utilDate = new SimpleDateFormat("dd-MM-yyyy").parse(date);
			outDate = new java.sql.Date(utilDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return outDate;
	}

	public static Date getCurrentDate() {
		java.sql.Date dateSql = null;
		LocalDate dateObj = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date = dateObj.format(formatter);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		try {
			java.util.Date utilDate = format.parse(date);
			dateSql = new java.sql.Date(utilDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateSql;
	}

	public static Time parseTime(String inputTime) {
		final String TIME_FORMAT_24HR = "HH:mm";
		final String TIME_FORMAT_24HRSS = "HH:mm:ss";
		final String TIME_FORMAT_12HR = "hh:mm a";
		final String TIME_FORMAT_12HRSS = "hh:mm:ss a";
		SimpleDateFormat sdf = null;

		try {
			sdf = new SimpleDateFormat(TIME_FORMAT_24HR);
			return new Time(sdf.parse(inputTime).getTime());
		} catch (ParseException e) {
			try {
				sdf = new SimpleDateFormat(TIME_FORMAT_24HRSS);
				return new Time(sdf.parse(inputTime).getTime());
			} catch (ParseException e1) {

				try {
					sdf = new SimpleDateFormat(TIME_FORMAT_12HR);
					return new Time(sdf.parse(inputTime).getTime());
				} catch (ParseException e2) {
					try {
						sdf = new SimpleDateFormat(TIME_FORMAT_12HRSS);
						return new Time(sdf.parse(inputTime).getTime());
					} catch (ParseException e3) {
						// Invalid format - to add logger error
					}
				}
			}
		}

		return new Time(0);
	}

	public static Timestamp currentDateConvertStringToDate(String currentDate) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			LocalDateTime dateTime = LocalDateTime.parse(currentDate, formatter);

			return Timestamp.valueOf(dateTime);
		} catch (DateTimeParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
