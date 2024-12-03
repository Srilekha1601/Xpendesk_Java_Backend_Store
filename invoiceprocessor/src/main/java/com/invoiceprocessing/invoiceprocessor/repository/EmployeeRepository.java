package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	Optional<Employee> findByUserName(String userName);

	Optional<Employee> findByEmployeeId(Integer userId);

	List<Employee> findByAssistantId(Employee employeeId);

	List<Employee> findAllByManager(Employee manager);

}
