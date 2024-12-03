package com.invoiceprocessing.invoiceprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.VoucherProjects;

@Repository
public interface VoucherProjectsRepository extends JpaRepository<VoucherProjects, Integer> {

}
