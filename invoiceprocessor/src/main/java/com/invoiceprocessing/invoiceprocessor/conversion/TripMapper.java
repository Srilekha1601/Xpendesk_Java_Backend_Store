//package com.invoiceprocessing.invoiceprocessor.conversion;
//
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.Named;
//import org.mapstruct.factory.Mappers;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.invoiceprocessing.invoiceprocessor.model.Employee;
//import com.invoiceprocessing.invoiceprocessor.model.Trip;
//import com.invoiceprocessing.invoiceprocessor.response.TripDTO;
////import com.invoiceprocessing.invoiceprocessor.service.TripServiceImpl;
//
//import jakarta.transaction.Transactional;
//
//@Mapper(componentModel = "spring")
//public interface TripMapper {
//
////	public static final TripServiceImpl tripServiceImpl = new TripServiceImpl();
//	TripMapper INSTANCE = Mappers.getMapper(TripMapper.class);
//
////	@Mapping(target = "employeeID", expression = "java(tripServiceImpl.getEmployeeByEmployeeId(Integer.parseInt(tripDTo.getEmployeeID())))")
//	@Mapping(source = "tripDTo.expenseStatus", target = "expenseStatus", defaultValue = "D")
//	Trip dtoToModel(TripDTO tripDTo);
//
//	@Mapping(source = "trip.employeeID", target = "employeeID", qualifiedByName = "EmployeeToEmployeeID")
//	@Mapping(source = "trip.expenseStatus", target = "expenseStatus", defaultValue = "D")
//	TripDTO modelToDto(Trip trip);
//
//	@Named("employeeIDToemployee")
//	@Transactional
//	public static String employeeIDToemployee(Employee employee) {
//		return employee.getEmployeeID().toString();
//	}
//	
//	@Named("EmployeeToEmployeeID")
//	@Transactional
//	public static String employeeToEmployeeId(Employee employee) {
//		return employee.getEmployeeID().toString();
//	}
//}