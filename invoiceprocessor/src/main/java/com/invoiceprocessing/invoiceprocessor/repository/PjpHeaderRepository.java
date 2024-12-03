package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.Employee;
import com.invoiceprocessing.invoiceprocessor.model.PjpHeader;

@Repository
public interface PjpHeaderRepository extends JpaRepository<PjpHeader, Integer> {

	Optional<PjpHeader> findByEmployeeIdAndMonthAndYear(Employee employee, Integer month, Integer year);

}
