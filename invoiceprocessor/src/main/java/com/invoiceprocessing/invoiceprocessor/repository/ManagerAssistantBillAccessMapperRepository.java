package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.Employee;
import com.invoiceprocessing.invoiceprocessor.model.ManagerAssistantBillAccessMapper;

@Repository
public interface ManagerAssistantBillAccessMapperRepository
		extends JpaRepository<ManagerAssistantBillAccessMapper, Integer> {

	List<ManagerAssistantBillAccessMapper> findByManager(Employee manager);

	@Query(value = "select mam from ManagerAssistantBillAccessMapper mam where mam.manager = :manager and mam.assistantId = :assistantId order by mam.createdOn desc")
	List<ManagerAssistantBillAccessMapper> findByManagerAndAssistantIdOrderByCreatedOnDesc(
			@Param("manager") Employee manager, @Param("assistantId") Integer assistantId);

	List<ManagerAssistantBillAccessMapper> findByManagerOrderByCreatedOnDesc(Employee manager);

	List<ManagerAssistantBillAccessMapper> findByAssistantId(Integer assistantId);

	List<ManagerAssistantBillAccessMapper> findByAssistantIdOrderByCreatedOnDesc(Integer assistantId);

}
