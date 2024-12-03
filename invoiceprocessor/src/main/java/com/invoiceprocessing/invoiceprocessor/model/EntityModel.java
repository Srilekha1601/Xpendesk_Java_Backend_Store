package com.invoiceprocessing.invoiceprocessor.model;

import java.sql.Date;

import com.google.protobuf.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Entity")
public class EntityModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "entity_id")
	private Integer entityId;

	@Column(name = "entity_name", length = 100)
	private String entityName;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;

	@ManyToOne
	@JoinColumn(name = "client_id", referencedColumnName = "client_id")
	private Client clientId;

	@Column(name = "gstin", length = 15)
	private String gstIn;

	@Column(name = "is_deleted", length = 255)
	private String isDeleted;

	@Column(name = "created_by", length = 10)
	private String createdBy;

	@Column(name = "created_on", length = 6)
	private Timestamp createdOn;

	@Column(name = "updated_by", length = 10)
	private String updatedBy;

	@Column(name = "updated_on", length = 6)
	private Timestamp updatedOn;

}
