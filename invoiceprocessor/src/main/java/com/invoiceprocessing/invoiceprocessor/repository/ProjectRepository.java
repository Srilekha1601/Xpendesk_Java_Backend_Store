package com.invoiceprocessing.invoiceprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

	Project findByProjectCode(String projectCode);

}
