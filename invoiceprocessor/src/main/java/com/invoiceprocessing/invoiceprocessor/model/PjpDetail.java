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

@Entity
@Table(name = "PJP_DETAIL")
@Data
public class PjpDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pjp_detail_id")
	private Integer pjpDetailId;

	@ManyToOne
	@JoinColumn(name = "pjp_id", referencedColumnName = "pjp_id")
	private PjpHeader pjpId;

	@Column(name = "pjp_date")
	private String pjpDate;

	@Column(name = "pjp_description", length = 100)
	private String pjpDescription;

}
