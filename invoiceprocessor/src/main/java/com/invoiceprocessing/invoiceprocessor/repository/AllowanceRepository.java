package com.invoiceprocessing.invoiceprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.Allowance;

@Repository
public interface AllowanceRepository extends JpaRepository<Allowance, Integer>{

}