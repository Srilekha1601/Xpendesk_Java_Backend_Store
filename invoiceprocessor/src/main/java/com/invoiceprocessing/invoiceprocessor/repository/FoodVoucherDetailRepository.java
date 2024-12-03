package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.FoodVoucherDetail;
import com.invoiceprocessing.invoiceprocessor.model.HotelVoucher;

@Repository
public interface FoodVoucherDetailRepository extends JpaRepository<FoodVoucherDetail, Integer> {

	@Modifying
	@Query(value = "delete from food_voucher_detail where HOTEL_VOUCHER_ID = \r\n"
			+ "(select HOTEL_VOUCHER_ID from hotel_voucher where VOUCHER_ID = :voucherId)\r\n" + "", nativeQuery = true)
	public void deleteFoodVoucherDetailForHotelVoucher(@Param("voucherId") Integer voucherId);

	public List<FoodVoucherDetail> findByHotelVoucher(HotelVoucher hotelVoucher);

}
