package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.ConsolidatedExpenseDetail;

@Repository
public interface ConsolidatedExpenseDetailRepository extends JpaRepository<ConsolidatedExpenseDetail, Integer> {

	@Query("SELECT c FROM ConsolidatedExpenseDetail c WHERE c.voucherIds LIKE %?1%")
	List<ConsolidatedExpenseDetail> existByVoucherId(String voucherIds);

}
