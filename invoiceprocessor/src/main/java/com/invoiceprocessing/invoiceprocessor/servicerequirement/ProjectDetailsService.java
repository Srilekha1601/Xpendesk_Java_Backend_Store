package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.response.ProjectDto;

public interface ProjectDetailsService {

	List<ProjectDto> getDetailsForProject();

}
