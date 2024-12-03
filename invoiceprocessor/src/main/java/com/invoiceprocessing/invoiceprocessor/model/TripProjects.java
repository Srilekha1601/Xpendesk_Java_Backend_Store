package com.invoiceprocessing.invoiceprocessor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "TRIP_PROJECTS")
public class TripProjects {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRIP_PROJECT_ID")
	private Integer tripProjectId;

	@ManyToOne
	@JoinColumn(name = "TRIP_ID", referencedColumnName = "TRIP_ID")
	private Trip tripId;

	@ManyToOne
	@JoinColumn(name = "PROJECT_ID", referencedColumnName = "project_id")
	private Project projectId;

}
