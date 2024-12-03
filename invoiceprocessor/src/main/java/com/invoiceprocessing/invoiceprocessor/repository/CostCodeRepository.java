package com.invoiceprocessing.invoiceprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.CostCode;

@Repository
public interface CostCodeRepository extends JpaRepository<CostCode, Integer> {

	CostCode findByCostCode(String code);

}
