package com.invoiceprocessing.invoiceprocessor.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.invoiceprocessing.invoiceprocessor.markerentity.TripEntity;
import com.invoiceprocessing.invoiceprocessor.model.CostCode;
import com.invoiceprocessing.invoiceprocessor.model.Employee;
import com.invoiceprocessing.invoiceprocessor.model.Project;
import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.model.TripCosts;
import com.invoiceprocessing.invoiceprocessor.model.TripProjects;
import com.invoiceprocessing.invoiceprocessor.repository.CostCodeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.ProjectRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripCostsRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripProjectsRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripRepository;
import com.invoiceprocessing.invoiceprocessor.response.TripDTO;

@Component("trip")
public class TripMapperImpl implements TripMapper<TripDTO, TripEntity> {

	@Autowired
	TripRepository tripRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	CostCodeRepository costCodeRepository;

	@Autowired
	TripProjectsRepository tripProjectsRepository;

	@Autowired
	TripCostsRepository tripCostsRepository;

	@Override
	public TripEntity dtoToModel(TripDTO tripDto) {

		Trip trip = new Trip();
		Project project = new Project();
		CostCode costCodeEntity = new CostCode();
		TripProjects tripProjects = null;
		List<TripProjects> listOfTripProject = new ArrayList<TripProjects>();
		TripCosts tripCosts = null;
		List<TripCosts> listOfTripCost = new ArrayList<TripCosts>();

		if (tripDto != null) {

			trip.setFromDate(parseStringToDate(tripDto.getFromDate()));
			trip.setToDate(parseStringToDate(tripDto.getToDate()));
			trip.setExpenseStatus(tripDto.getExpenseStatus());
			trip.setEmployeeID(getEmployeeEmpId(Integer.parseInt(tripDto.getEmployeeID())));
			trip.setTripName(tripDto.getTripName());
			trip = tripRepository.save(trip);

			if (!tripDto.getProjectCodes().isEmpty()) {
				for (String projectCode : tripDto.getProjectCodes()) {

					tripProjects = new TripProjects();
					tripProjects.setTripId(trip);
					project = projectRepository.findByProjectCode(projectCode);
					tripProjects.setProjectId(project);

					listOfTripProject.add(tripProjects);

				}
				tripProjectsRepository.saveAll(listOfTripProject);
			}

			if (!tripDto.getCostCodes().isEmpty()) {
				for (String costCode : tripDto.getCostCodes()) {

					tripCosts = new TripCosts();
					tripCosts.setTripId(trip);
					costCodeEntity = costCodeRepository.findByCostCode(costCode);
					tripCosts.setCostId(costCodeEntity);

					listOfTripCost.add(tripCosts);

				}
				tripCostsRepository.saveAll(listOfTripCost);
			}
		}

		return trip;

	}

	@Override
	public TripDTO modelToDto(TripEntity target) {
		// TODO Auto-generated method stub
		return null;
	}

	private static java.sql.Date parseStringToDate(String date) {
		java.sql.Date outDate = null;
		try {
			java.util.Date utilDate = new SimpleDateFormat("dd-MM-yyyy").parse(date);
			outDate = new java.sql.Date(utilDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return outDate;
	}

	private Employee getEmployeeEmpId(Integer employeeId) {
		Optional<Employee> employee = employeeRepository.findById(employeeId);
		return employee.get();
	}

}
