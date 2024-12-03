package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.CreditCardTransactions;
import com.invoiceprocessing.invoiceprocessor.model.Employee;

@Repository
public interface CreditCardTransactionsRepository extends JpaRepository<CreditCardTransactions, Integer> {

	List<CreditCardTransactions> findAllByEmployee(Employee employee);

}
