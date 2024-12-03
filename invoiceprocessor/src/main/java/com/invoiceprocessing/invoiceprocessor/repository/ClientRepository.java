package com.invoiceprocessing.invoiceprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

}
