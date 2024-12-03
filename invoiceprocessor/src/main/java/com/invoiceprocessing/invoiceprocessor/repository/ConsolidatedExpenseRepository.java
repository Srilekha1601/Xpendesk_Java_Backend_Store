package com.invoiceprocessing.invoiceprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.ConsolidatedExpense;

@Repository
public interface ConsolidatedExpenseRepository extends JpaRepository<ConsolidatedExpense, Integer>{

}
