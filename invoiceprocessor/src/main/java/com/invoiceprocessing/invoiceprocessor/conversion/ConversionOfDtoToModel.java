package com.invoiceprocessing.invoiceprocessor.conversion;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.invoiceprocessing.invoiceprocessor.model.Employee;
import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripRepository;
import com.invoiceprocessing.invoiceprocessor.response.TripDTO;

@Component("tripinformation")
public class ConversionOfDtoToModel {

	@Autowired
	EmployeeRepository employeeRepo;
	@Autowired 
	TripRepository tripRepo;

	public Trip dtoToModel(TripDTO tripDto) {
		Employee employee = getEmployeeByEmployeeId(Integer.parseInt(tripDto.getEmployeeID()));
		Trip trip = new Trip();
		if (tripDto != null) {
			trip.setExpenseStatus(tripDto.getExpenseStatus());
			trip.setToDate(parseStringToDate(tripDto.getToDate()));
			trip.setFromDate(parseStringToDate(tripDto.getFromDate()));
			trip.setEmployeeID(employee);
			tripRepo.save(trip);
		} else {
			return null;
		}

		return trip;
	}

	public TripDTO tripToTripDto(Trip trip) {
		TripDTO tripDto = new TripDTO();
//		tripDto.setEmployeeID(trip.getEmployeeID().getEmployeeID().toString());
//		tripDto.setFromDate(trip.getFromDate());
//		tripDto.setToDate(trip.getToDate());
//		tripDto.setExpenseStatus(trip.getExpenseStatus());
//
		return tripDto;
	}

	private Employee getEmployeeByEmployeeId(Integer employeeId) {
		Optional<Employee> employee = employeeRepo.findById(employeeId);
		Employee emp = employee.get();
		return emp;
	}

	private static Date parseStringToDate(String date) {
		Date outDate = null;
		try {
			outDate = new java.sql.Date(((java.util.Date) new SimpleDateFormat("yyyy-MM-dd").parse(date)).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return outDate;
	}

}
