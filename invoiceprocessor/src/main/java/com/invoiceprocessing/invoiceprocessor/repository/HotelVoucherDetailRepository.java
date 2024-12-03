package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.HotelVoucher;
import com.invoiceprocessing.invoiceprocessor.model.HotelVoucherDetail;

@Repository
public interface HotelVoucherDetailRepository extends JpaRepository<HotelVoucherDetail, Integer> {

	@Modifying
	@Query(value = "delete from hotel_voucher_detail where HOTEL_VOUCHER_ID = \r\n"
			+ "(select HOTEL_VOUCHER_ID from hotel_voucher where VOUCHER_ID = :voucherId)\r\n" + "", nativeQuery = true)
	public void deleteHotelVoucherDetailForHotelVoucher(@Param("voucherId") Integer voucherId);

	List<HotelVoucherDetail> findByHotelVoucherID(HotelVoucher hotelVoucher);

}
