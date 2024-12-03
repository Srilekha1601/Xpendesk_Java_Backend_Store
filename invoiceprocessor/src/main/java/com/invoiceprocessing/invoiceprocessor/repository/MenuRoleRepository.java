package com.invoiceprocessing.invoiceprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.MenuRoleCrossRef;

@Repository
public interface MenuRoleRepository extends JpaRepository<MenuRoleCrossRef, Integer>{

}
