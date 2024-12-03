package com.invoiceprocessing.invoiceprocessor.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.model.Project;
import com.invoiceprocessing.invoiceprocessor.repository.ProjectRepository;
import com.invoiceprocessing.invoiceprocessor.response.ProjectDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.ProjectDetailsService;

@Service
public class ProjectDetailsServiceImpl implements ProjectDetailsService {

	@Autowired
	ProjectRepository projectRepository;

	@Override
	public List<ProjectDto> getDetailsForProject() {

		List<Project> listOfProjects = projectRepository.findAll();
		return listOfProjectToProjectDto(listOfProjects);

	}

	private List<ProjectDto> listOfProjectToProjectDto(List<Project> listOfProjects) {

		ProjectDto projectDto = null;
		List<ProjectDto> listOfProjectDtos = new ArrayList<ProjectDto>();

		for (Project project : listOfProjects) {

			projectDto = new ProjectDto();
			projectDto.setIsActive(project.getIsActive());
			projectDto.setProjectCode(project.getProjectCode());
			projectDto.setProjectDescription(project.getProjectDescription());
			projectDto.setProjectId(project.getProjectId());

			listOfProjectDtos.add(projectDto);

		}

		return listOfProjectDtos;

	}

}
