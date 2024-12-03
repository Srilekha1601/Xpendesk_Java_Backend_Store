package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.CreditCardTransactions;
import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

	List<Voucher> findByTrip(Trip tripId);

	List<Voucher> findByCreditCardTransactionsOrderByVoucherIDDesc(CreditCardTransactions creditCardTransactions);

	@Query("SELECT count(v.hashText) FROM Voucher v where v.hashText = :hashText")
	Integer existByHashValue(@Param("hashText") String hashText);

}
