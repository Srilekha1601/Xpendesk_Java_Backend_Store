package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.ConsolidatedBreakageSummary;
import com.invoiceprocessing.invoiceprocessor.model.Trip;

@Repository
public interface InvoiceConsolidatedSummeryRepository extends JpaRepository<ConsolidatedBreakageSummary, Integer> {

	@Query(value = "SELECT * FROM\r\n" + "(\r\n"
			+ "select 1 as TRIP_CONSOLIDATION_ID, t.TRIP_ID as TRIP_ID, hvd.BILL_TYPE as category, v.VOUCHER_DATE as \r\n"
			+ "VOUCHER_DATE, sum(hvd.AMOUNT) as AMOUNT, 0 as CLAIMED_AMOUNT, '' as EXCEPTION_REASON \r\n"
			+ "from trip t, voucher v, hotel_voucher hv, hotel_voucher_detail hvd\r\n"
			+ "where t.TRIP_ID = v.TRIP_ID\r\n" + "and v.VOUCHER_ID = hv.VOUCHER_ID\r\n"
			+ "and hv.HOTEL_VOUCHER_ID = hvd.HOTEL_VOUCHER_ID\r\n"
			+ "group by t.TRIP_ID, v.VOUCHER_DATE, hvd.BILL_TYPE\r\n" + "UNION\r\n"
			+ "select TRIP_CONSOLIDATION_ID, TRIP_ID, category, VOUCHER_DATE, sum(AMOUNT) as AMOUNT, sum(CLAIMED_AMOUNT) as CLAIMED_AMOUNT, max(EXCEPTION_REASON) as EXCEPTION_REASON \r\n"
			+ "from (select 1 as TRIP_CONSOLIDATION_ID, t.TRIP_ID as TRIP_ID, 'food' as category, v.VOUCHER_DATE as VOUCHER_DATE, sum(fvd.AMOUNT) as AMOUNT, 0 as CLAIMED_AMOUNT, '' as EXCEPTION_REASON \r\n"
			+ "from trip t, voucher v, food_voucher fv, food_voucher_detail fvd\r\n" + "where t.TRIP_ID = v.TRIP_ID\r\n"
			+ "and v.VOUCHER_ID = fv.VOUCHER_ID\r\n" + "and fv.FOOD_VOUCHER_ID = fvd.FOOD_VOUCHER_ID\r\n"
			+ "group by t.TRIP_ID, v.VOUCHER_DATE\r\n" + "UNION\r\n"
			+ "select 1 as TRIP_CONSOLIDATION_ID, t.TRIP_ID as TRIP_ID, 'food' as category, v.VOUCHER_DATE as VOUCHER_DATE, sum(fvd.AMOUNT) as AMOUNT, 0 as CLAIMED_AMOUNT, '' as EXCEPTION_REASON\r\n"
			+ "from trip t, voucher v, hotel_voucher hv, food_voucher_detail fvd\r\n"
			+ "where t.TRIP_ID = v.TRIP_ID\r\n" + "and v.VOUCHER_ID = hv.VOUCHER_ID\r\n"
			+ "and hv.HOTEL_VOUCHER_ID = fvd.HOTEL_VOUCHER_ID\r\n" + "group by t.TRIP_ID, v.VOUCHER_DATE\r\n"
			+ ") as food\r\n" + "group by TRIP_CONSOLIDATION_ID, TRIP_ID, category, VOUCHER_DATE\r\n" + "UNION\r\n"
			+ "select 1 as TRIP_CONSOLIDATION_ID, t.TRIP_ID as TRIP_ID, 'conveyance' as category, v.VOUCHER_DATE as VOUCHER_DATE, sum(v.TOTAL_AMOUNT) as AMOUNT, 0 as CLAIMED_AMOUNT, '' as EXCEPTION_REASON \r\n"
			+ "from trip t, voucher v, conveyance_voucher cv\r\n" + "where t.TRIP_ID = v.TRIP_ID\r\n"
			+ "and v.VOUCHER_ID = cv.VOUCHER_ID\r\n" + "group by t.TRIP_ID, v.VOUCHER_DATE\r\n" + ") cons\r\n"
			+ "WHERE cons.TRIP_ID = :tripId", nativeQuery = true)
	List<ConsolidatedBreakageSummary> findSummary(@Param("tripId") Integer tripID);

	List<ConsolidatedBreakageSummary> findByTrip(Trip trip);

	void deleteByTrip(Trip trip);

}
