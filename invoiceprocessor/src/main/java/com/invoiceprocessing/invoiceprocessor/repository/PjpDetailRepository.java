package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.PjpDetail;
import com.invoiceprocessing.invoiceprocessor.model.PjpHeader;

@Repository
public interface PjpDetailRepository extends JpaRepository<PjpDetail, Integer> {

	List<PjpDetail> findByPjpId(PjpHeader pjpHeader);

}
