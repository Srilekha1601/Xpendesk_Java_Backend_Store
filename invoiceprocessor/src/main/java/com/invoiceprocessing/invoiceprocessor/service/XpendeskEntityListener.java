package com.invoiceprocessing.invoiceprocessor.service;

import java.sql.Date;

import org.springframework.stereotype.Component;

import com.invoiceprocessing.invoiceprocessor.model.AuditFields;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRepository;
import com.invoiceprocessing.invoiceprocessor.utils.XpendeskUtils;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Component
public class XpendeskEntityListener {

	EmployeeRepository employeeRepository;

//	private static ApplicationContext applicationContext;

	@PrePersist
	public void prePersist(AuditFields entity) {
		entity.setIsDeleted("N");
		entity.setCreatedBy(XpendeskUtils.extractLoggedInUserId());
		entity.setCreatedOn(new Date((new java.util.Date()).getTime()));
		entity.setUpdatedBy(XpendeskUtils.extractLoggedInUserId());
		entity.setUpdatedOn(new Date((new java.util.Date()).getTime()));
	}

	@PreUpdate
	public void preUpdate(AuditFields entity) {
		entity.setUpdatedBy(XpendeskUtils.extractLoggedInUserId());
		entity.setUpdatedOn(new Date((new java.util.Date()).getTime()));
	}

//	@Override
//	public void setApplicationContext(ApplicationContext context) throws BeansException {
//		applicationContext = context;
//	}
}
