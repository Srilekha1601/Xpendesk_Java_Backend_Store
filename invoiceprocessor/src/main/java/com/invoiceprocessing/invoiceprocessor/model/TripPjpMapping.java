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
@Table(name = "TRIP_PJP_MAPPING")
public class TripPjpMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "trip_pjp_id")
	private Integer tripPjpId;

	@ManyToOne
	@JoinColumn(name = "trip_id", referencedColumnName = "TRIP_ID")
	private Trip trip;

	@ManyToOne
	@JoinColumn(name = "pjp_detail_id", referencedColumnName = "pjp_detail_id")
	private PjpDetail pjpDetail;

}
