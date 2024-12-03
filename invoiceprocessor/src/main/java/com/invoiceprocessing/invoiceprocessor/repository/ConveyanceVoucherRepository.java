package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.ConveyanceVoucher;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;

@Repository
public interface ConveyanceVoucherRepository extends JpaRepository<ConveyanceVoucher, Integer> {

	List<ConveyanceVoucher> findByVoucherID(Voucher voucher);

}
