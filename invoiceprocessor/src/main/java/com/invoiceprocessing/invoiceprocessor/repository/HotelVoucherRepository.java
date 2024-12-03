package com.invoiceprocessing.invoiceprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.HotelVoucher;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;

@Repository
public interface HotelVoucherRepository extends JpaRepository<HotelVoucher, Integer> {

	HotelVoucher findByVoucherID(Voucher voucher);

}
