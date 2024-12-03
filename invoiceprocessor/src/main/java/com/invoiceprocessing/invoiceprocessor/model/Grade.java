package com.invoiceprocessing.invoiceprocessor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "GRADE")
public class Grade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GRADE_ID")
	private Integer generateID;

	@Column(name = "GRADE", nullable = false, length = 50)
	private String grade;

	@Column(name = "DESIGNATION", nullable = false, length = 100)
	private String designation;

	@ManyToOne
	@JoinColumn(name = "workflow_id")
	private Workflow workflow;
	
	public Integer getGenerateID() {
		return generateID;
	}

	public void setGenerateID(Integer generateID) {
		this.generateID = generateID;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	
	

}
