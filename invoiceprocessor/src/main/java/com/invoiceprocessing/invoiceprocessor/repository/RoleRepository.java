package com.invoiceprocessing.invoiceprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	Role findByRoleDescription(String roleDescription);

}