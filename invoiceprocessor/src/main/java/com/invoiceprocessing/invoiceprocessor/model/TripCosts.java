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
@Table(name = "TRIP_COSTS")
public class TripCosts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRIP_COST_ID")
	private Integer tripCostId;

	@ManyToOne
	@JoinColumn(name = "TRIP_ID", referencedColumnName = "TRIP_ID")
	private Trip tripId;

	@ManyToOne
	@JoinColumn(name = "COST_ID", referencedColumnName = "cost_id")
	private CostCode costId;

}
