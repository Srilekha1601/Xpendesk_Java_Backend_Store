package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.EmployeeRole;
import com.invoiceprocessing.invoiceprocessor.model.Role;

@Repository
public interface EmployeeRoleRepository extends JpaRepository<EmployeeRole, Integer> {

	List<EmployeeRole> findByRoleID(Role role);

}
