package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.FoodVoucher;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;

@Repository
public interface FoodVoucherRepository extends JpaRepository<FoodVoucher, Integer> {

	List<FoodVoucher> findByVoucherID(Voucher voucher);

}
