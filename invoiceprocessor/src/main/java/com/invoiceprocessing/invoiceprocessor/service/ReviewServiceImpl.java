package com.invoiceprocessing.invoiceprocessor.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.model.ConsolidatedBreakageSummary;
import com.invoiceprocessing.invoiceprocessor.model.ConsolidatedExpenseDetail;
import com.invoiceprocessing.invoiceprocessor.model.TripApprovedAmount;
//import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.repository.ConsolidatedExpenseDetailRepository;
//import com.invoiceprocessing.invoiceprocessor.model.FoodVoucher;
//import com.invoiceprocessing.invoiceprocessor.model.Trip;
//import com.invoiceprocessing.invoiceprocessor.model.Voucher;
//import com.invoiceprocessing.invoiceprocessor.model.HotelVoucherDetail;
//import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.repository.FoodVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.HotelVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.InvoiceConsolidatedSummeryRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripApprovedAmountRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherRepository;
import com.invoiceprocessing.invoiceprocessor.response.ConsolidatedBreakageSummaryDto;
import com.invoiceprocessing.invoiceprocessor.response.PartApprovalDto;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;
import com.invoiceprocessing.invoiceprocessor.response.ReviewForVouchersAttributesDto;
import com.invoiceprocessing.invoiceprocessor.response.ReviewInvoicesDto;
//import com.invoiceprocessing.invoiceprocessor.response.VoucherDetailDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;
import com.invoiceprocessing.invoiceprocessor.response.WorkflowTaskDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.ReviewService;
//import com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants;
//import com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants;
import com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants;

import io.micrometer.common.util.StringUtils;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Value("${xpendesk.db.url}")
	private String url;

	@Value("${xpendesk.db.user}")
	private String user;

	@Value("${xpendesk.db.password}")
	private String password;

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	TripRepository tripRepository;

	@Autowired
	HotelVoucherRepository hotelVoucherRepository;

	@Autowired
	FoodVoucherRepository foodVoucherRepository;

	@Autowired
	private WorkflowServiceImpl workflowServiceImpl;

	@Autowired
	InvoiceConsolidatedSummeryRepository invoiceConsolidatedSummeryRepository;
	
	@Autowired
	TripApprovedAmountRepository tripApprovedAmountRepository;

	@Autowired
	ConsolidatedExpenseDetailRepository consolidatedExpenseDetailRepository;
	
	@Autowired
	RuleServiceImpl ruleService;
	
	public static List<ConsolidatedBreakageSummaryDto> transformDtos(List<ConsolidatedBreakageSummaryDto> listOfConsolidatedBreakageSummaryDtos) {
	    List<ConsolidatedBreakageSummaryDto> transformedList = new ArrayList<>();

	    for (ConsolidatedBreakageSummaryDto originalDto : listOfConsolidatedBreakageSummaryDtos) {
	        // Create a new instance of the DTO and set the required fields
	        ConsolidatedBreakageSummaryDto newDto = new ConsolidatedBreakageSummaryDto();
	        
	        // Copy properties and apply transformations as needed
	        newDto.setTripID(originalDto.getTripID());
	        newDto.setTripConsolidationId(originalDto.getTripConsolidationId());
	        newDto.setCategory(originalDto.getCategory());

	        // Set consolidatedDate to the value of date
	        newDto.setConsolidatedDate(originalDto.getDate());

	        // Set grade to "A" and location to "Class A"
	        newDto.setGrade("A");
	        newDto.setLocation("Class A");

	        // Copy other properties from the original DTO
	        newDto.setDate(originalDto.getDate());
	        newDto.setPartApproval(originalDto.getPartApproval());
	        newDto.setContainsLiquor(originalDto.getContainsLiquor());
	        newDto.setFoodVoucherId(originalDto.getFoodVoucherId());
	        newDto.setHotelVoucherId(originalDto.getHotelVoucherId());
	        newDto.setObjectionRaise(originalDto.getObjectionRaise());
	        newDto.setConveyanceVoucherId(originalDto.getConveyanceVoucherId());
	        newDto.setAmount(originalDto.getClaimedAmount());
	        newDto.setEligibility(originalDto.getEligibility());
	        newDto.setVoucherID(originalDto.getVoucherID());
	        newDto.setIsSingleExpense(originalDto.getIsSingleExpense());
	        newDto.setEligible(originalDto.isEligible());
	        newDto.setApprovedAmount(originalDto.getApprovedAmount());
	        newDto.setRejectedAmount(originalDto.getRejectedAmount());
	        newDto.setIsFinalSubmit(originalDto.getIsFinalSubmit());
	        newDto.setExceptionReason(originalDto.getExceptionReason());
	        newDto.setClaimedAmount(originalDto.getClaimedAmount());
	        newDto.setMonthlyCap(originalDto.getMonthlyCap());

	        // Add the transformed DTO to the list
	        transformedList.add(newDto);
	    }

	    // Return the transformed list
	    return transformedList;
	}
	@Override
	public List<VoucherDto> reviewVouchers(ReviewForVouchersAttributesDto reviewForVouchersAttributesDto) {
		// TODO Auto-generated method stub
//
//		VoucherDto voucherDto = null;
//		
//		List<Voucher> vouchers = tripRepository.findById(reviewForVouchersAttributesDto.getTripId()).get()
//				.getVouchers();
//
//		for (Voucher voucher : vouchers) {
//			voucher.getHotelVoucherID().getListOfHotelVoucherDetail().forEach(hotelVoucherDetails -> {
//				if (new SimpleDateFormat("dd-MM-yyyy").format(hotelVoucherDetails.getDate())
//						.equalsIgnoreCase(reviewForVouchersAttributesDto.getDate())) {
//					VoucherDetailDto voucherDetailDto = new VoucherDetailDto();
//					if(hotelVoucherDetails.getBillType().equalsIgnoreCase(XpendeskConstants.ACCOMMODATION_TYPE)) {
//						
//					}
//				}
//			});
//			voucherDto = new VoucherDto();
//		}
		return null;
	}

	@Override
	public List<ConsolidatedBreakageSummaryDto> getListOfConsolidatedSummary(ReviewInvoicesDto reviewInvoicesDto) {
		// TODO Auto-generated method stub

		List<ConsolidatedBreakageSummaryDto> listOfConsolidatedBreakageSummaryDtos = new ArrayList<ConsolidatedBreakageSummaryDto>();
		ConsolidatedBreakageSummaryDto consolidatedBreakageSummaryDto = null;
		String query = "SELECT \r\n" + "cbs.TRIP_CONSOLIDATION_ID, \r\n" + "cbs.TRIP_ID, \r\n"
				+ "    cbs.CONSOLIDATION_DATE as DATE, \r\n"
				+ "cbs.CLAIMED_AMOUNT, cbs.APPROVED_AMOUNT, cbs.REJECTED_AMOUNT,\r\n" + "cbs.EXCEPTION_REASON,\r\n"
				+ "cbs.CATEGORY,\r\n"
				+ "    CASE WHEN cbs.CATEGORY = 'food' THEN x.CONTAINS_LIQUOR ELSE NULL END AS CONTAINS_LIQUOR,\r\n"
				+ "    CASE WHEN cbs.CATEGORY = 'food' THEN x.OBJECTION_RAISE ELSE NULL END AS OBJECTION_RAISE\r\n"
				+ "FROM \r\n" + "consolidatedbreakagesummary cbs \r\n" + "LEFT JOIN \r\n" + "(SELECT \r\n"
				+ "        v.trip_id, \r\n" + "v.voucher_date, \r\n"
				+ "        CASE WHEN SUM(fv.contains_liquor) > 0 THEN 'Yes' ELSE 'No' END as contains_liquor,\r\n"
				+ "        MAX(fv.objection_raise) as OBJECTION_RAISE\r\n" + "FROM \r\n" + "voucher v \r\n"
				+ "LEFT JOIN \r\n" + "food_voucher fv ON v.VOUCHER_ID = fv.voucher_id\r\n" + "WHERE \r\n"
				+ "        v.trip_id = ?\r\n" + "GROUP BY \r\n" + "v.trip_id, \r\n" + "v.voucher_date\r\n" + ") x \r\n"
				+ "ON \r\n" + "x.TRIP_ID = cbs.TRIP_ID \r\n" + "AND x.voucher_date = cbs.CONSOLIDATION_DATE\r\n"
				+ "WHERE \r\n" + "cbs.trip_id = ? \r\n" + "ORDER BY \r\n" + "cbs.CONSOLIDATION_DATE DESC;";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, reviewInvoicesDto.getReferenceId());
			preparedStatement.setInt(2, reviewInvoicesDto.getReferenceId());
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					consolidatedBreakageSummaryDto = new ConsolidatedBreakageSummaryDto();
					consolidatedBreakageSummaryDto.setTripID(resultSet.getInt("TRIP_ID"));
					consolidatedBreakageSummaryDto.setDate(resultSet.getString("DATE"));
					consolidatedBreakageSummaryDto.setClaimedAmount(resultSet.getBigDecimal("CLAIMED_AMOUNT"));
					consolidatedBreakageSummaryDto.setExceptionReason(resultSet.getString("EXCEPTION_REASON"));
					consolidatedBreakageSummaryDto.setCategory(resultSet.getString("CATEGORY"));
					consolidatedBreakageSummaryDto.setObjectionRaise(resultSet.getString("OBJECTION_RAISE"));
					consolidatedBreakageSummaryDto.setTripConsolidationId(resultSet.getInt("TRIP_CONSOLIDATION_ID"));
					consolidatedBreakageSummaryDto.setContainsLiquor(resultSet.getString("CONTAINS_LIQUOR"));
					consolidatedBreakageSummaryDto
							.setApprovedAmount(resultSet.getBigDecimal("APPROVED_AMOUNT") == null ? new BigDecimal(0)
									: resultSet.getBigDecimal("APPROVED_AMOUNT"));
					consolidatedBreakageSummaryDto
							.setRejectedAmount(resultSet.getBigDecimal("REJECTED_AMOUNT") == null ? new BigDecimal(0)
									: resultSet.getBigDecimal("REJECTED_AMOUNT"));

					listOfConsolidatedBreakageSummaryDtos.add(consolidatedBreakageSummaryDto);
					

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}


		//System.out.println("getList cons summary");
		//System.out.println(listOfConsolidatedBreakageSummaryDtos);
		//System.out.println(transformDtos(listOfConsolidatedBreakageSummaryDtos));
		//ruleService.eligibilityCriteriaService(transformDtos(listOfConsolidatedBreakageSummaryDtos));
		//return listOfConsolidatedBreakageSummaryDtos;
		return ruleService.eligibilityCriteriaService(transformDtos(listOfConsolidatedBreakageSummaryDtos));

		
	}

//	@Override
//	public List<VoucherWithOutTripDto> getNonTripVoucherSummary(ReviewInvoicesDto reviewInvoicesDto) {
//		// TODO Auto-generated method stub
//
//		List<VoucherWithOutTripDto> listOfVoucherWithOutTripDtos = new ArrayList<VoucherWithOutTripDto>();
//		VoucherWithOutTripDto voucherWithOutTripDto = new VoucherWithOutTripDto();
//		Voucher voucher = voucherRepository.findById(reviewInvoicesDto.getReferenceId()).orElse(null);
//
//		voucherWithOutTripDto.setClaimedAmount(voucher.getClaimedAmount().toString());
//		voucherWithOutTripDto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(voucher.getInvoiceDate()));
//
//		listOfVoucherWithOutTripDtos.add(voucherWithOutTripDto);
//
//		return listOfVoucherWithOutTripDtos;
//	}

	@Override
	public List<VoucherWithOutTripDto> getNonTripVoucherSummary(ReviewInvoicesDto reviewInvoicesDto) {
		// TODO Auto-generated method stub

		List<VoucherWithOutTripDto> listOfVoucherWithOutTripDtos = new ArrayList<VoucherWithOutTripDto>();
		VoucherWithOutTripDto voucherWithOutTripDto = new VoucherWithOutTripDto();

		String query = "SELECT \r\n" + "v.VOUCHER_ID, \r\n" + "v.INVOICE_TYPE,\r\n" + "v.VOUCHER_DATE,\r\n"
				+ "    v.INVOICE_DATE,\r\n" + "v.APPROVED_AMOUNT,\r\n" + "v.CLAIMED_AMOUNT,\r\n" + "CASE \r\n"
				+ "        WHEN v.INVOICE_TYPE = 'food' THEN \r\n" + "CASE \r\n"
				+ "                WHEN fv.CONTAINS_LIQUOR = 1 THEN 'Yes' \r\n"
				+ "                WHEN fv.CONTAINS_LIQUOR = 0 THEN 'No'\r\n" + "END\r\n" + "ELSE NULL \r\n"
				+ "END AS CONTAINS_LIQUOR,\r\n" + "fv.OBJECTION_RAISE\r\n" + "FROM \r\n" + "voucher v\r\n"
				+ "LEFT JOIN \r\n" + "    food_voucher fv ON v.VOUCHER_ID = fv.VOUCHER_ID\r\n" + "WHERE \r\n"
				+ "v.voucher_id = ?";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, reviewInvoicesDto.getReferenceId());
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					voucherWithOutTripDto.setClaimedAmount(resultSet.getString("CLAIMED_AMOUNT"));
					voucherWithOutTripDto
							.setDate(new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("VOUCHER_DATE")));
					voucherWithOutTripDto.setBillType(resultSet.getString("INVOICE_TYPE"));
					voucherWithOutTripDto.setVoucherID(resultSet.getString("VOUCHER_ID"));
					voucherWithOutTripDto.setLiquorStatus(resultSet.getString("CONTAINS_LIQUOR"));
					voucherWithOutTripDto.setObjectionRaise(resultSet.getString("OBJECTION_RAISE"));
					voucherWithOutTripDto.setApprovedAmount(resultSet.getBigDecimal("APPROVED_AMOUNT"));

					listOfVoucherWithOutTripDtos.add(voucherWithOutTripDto);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listOfVoucherWithOutTripDtos;
	}

	@Override
	public List<ConsolidatedBreakageSummaryDto> saveConsolidatedSummary(
			List<ConsolidatedBreakageSummaryDto> listOfConsolidatedBreakageSummaryDtos) {
		// TODO Auto-generated method stub
		List<ConsolidatedBreakageSummary> listOfConsolidatedBreakageSummaries = new ArrayList<ConsolidatedBreakageSummary>();
		for (ConsolidatedBreakageSummaryDto consolidatedInvoice : listOfConsolidatedBreakageSummaryDtos) {
			ConsolidatedBreakageSummary consolidatedBreakageSummary = invoiceConsolidatedSummeryRepository
					.findById(consolidatedInvoice.getTripConsolidationId()).get();
			consolidatedBreakageSummary.setPartApproval(consolidatedInvoice.getPartApproval());
			listOfConsolidatedBreakageSummaries.add(consolidatedBreakageSummary);
		}

		invoiceConsolidatedSummeryRepository.saveAll(listOfConsolidatedBreakageSummaries);

		return listOfConsolidatedBreakageSummaryDtos;
	}

//	@Override
//	public ResponseMassageWithCodeDto partiallyApproval(List<PartApprovalDto> partApprovalDtoList) {
//
//		ResponseMassageWithCodeDto responseMassageWithCodeDto = new ResponseMassageWithCodeDto();
//
//		for (PartApprovalDto partApprovalDto : partApprovalDtoList) {
//
//			partApprovalDto.getListOfVoucherApprovalInfoDtos().forEach(voucherApproval -> {
//
//				Voucher voucher = voucherRepository.findById(voucherApproval.getVoucherId()).orElse(null);
//
//				if (voucher.getIsApproved() == null && voucherApproval.getIsApproved().equalsIgnoreCase("false")) {
//
//					updateRejectedVoucherAmountForConsolidationTable(voucher);
//
//					voucher.setIsApproved(XpendeskConstants.REJECT_APPROVAL);
//
//					voucherRepository.save(voucher);
//
//					responseMassageWithCodeDto.setMassage("Reject Successfully!!");
//					responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
//
//				} else if (voucher.getIsApproved() == null
//						&& voucherApproval.getIsApproved().equalsIgnoreCase("true")) {
//
//					updateApprovedAmountAndConsolidationTable(voucher);
//
//					voucher.setIsApproved(XpendeskConstants.VOUCHER_APPROVAL);
//
//					voucherRepository.save(voucher);
//
//					responseMassageWithCodeDto.setMassage("Approved Successfully!!");
//					responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
//
//				} else {
//					responseMassageWithCodeDto.setMassage("Already Updated!!");
//					responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
//				}
//
//			});
//
//		}
//
//		responseMassageWithCodeDto.setMassage("Approved Successfully!!");
//		responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
//
//		return responseMassageWithCodeDto;
//	}
//
//	private void updateApprovedAmountAndConsolidationTable(Voucher voucher) {
//		List<ConsolidatedExpenseDetail> listOfConsolidatedExpenseDetails = consolidatedExpenseDetailRepository
//				.existByVoucherId(voucher.getVoucherID().toString());
//
//		if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)) {
//			ConsolidatedExpenseDetail consolidatedExpenseDetail = listOfConsolidatedExpenseDetails.stream().findFirst()
//					.get();
//
//			String approvedVouchers = String.join(",",
//					Arrays.asList(consolidatedExpenseDetail.getVoucherIds().split(",")).stream()
//							.filter(voucherId -> voucherId.equalsIgnoreCase(voucher.getVoucherID().toString()))
//							.toList());
//
//			if (StringUtils.isBlank(consolidatedExpenseDetail.getApprovedVoucherIds())) {
//				consolidatedExpenseDetail.setApprovedVoucherIds(approvedVouchers);
//				consolidatedExpenseDetailRepository.save(consolidatedExpenseDetail);
//			} else {
//				String modifiedApprovedVouchers = String.join(",", consolidatedExpenseDetail.getApprovedVoucherIds(),
//						approvedVouchers);
//				consolidatedExpenseDetail.setApprovedVoucherIds(modifiedApprovedVouchers);
//				consolidatedExpenseDetailRepository.save(consolidatedExpenseDetail);
//			}
//
//			ConsolidatedBreakageSummary consolidatedBreakageSummary = invoiceConsolidatedSummeryRepository
//					.findById(consolidatedExpenseDetail.getConsolidatedBreakageSummary().getTripConsolidationID())
//					.orElse(null);
//
//			if (consolidatedBreakageSummary.getApprovedAmount() == null) {
//				consolidatedBreakageSummary.setApprovedAmount(voucher.getTotalAmount());
//				invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
//			} else {
//				BigDecimal modifiedApprovedAmount = consolidatedBreakageSummary.getApprovedAmount()
//						.add(voucher.getTotalAmount());
//				consolidatedBreakageSummary.setApprovedAmount(modifiedApprovedAmount);
//				invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
//			}
//
//		} else if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)) {
//			ConsolidatedExpenseDetail consolidatedExpenseDetail = listOfConsolidatedExpenseDetails.stream().findFirst()
//					.get();
//
//			String approvedVouchers = String.join(",",
//					Arrays.asList(consolidatedExpenseDetail.getVoucherIds().split(",")).stream()
//							.filter(voucherId -> voucherId.equalsIgnoreCase(voucher.getVoucherID().toString()))
//							.toList());
//
//			if (StringUtils.isBlank(consolidatedExpenseDetail.getApprovedVoucherIds())) {
//				consolidatedExpenseDetail.setApprovedVoucherIds(approvedVouchers);
//				consolidatedExpenseDetailRepository.save(consolidatedExpenseDetail);
//			} else {
//				String modifiedApprovedVouchers = String.join(",", consolidatedExpenseDetail.getApprovedVoucherIds(),
//						approvedVouchers);
//				consolidatedExpenseDetail.setApprovedVoucherIds(modifiedApprovedVouchers);
//				consolidatedExpenseDetailRepository.save(consolidatedExpenseDetail);
//			}
//
//			ConsolidatedBreakageSummary consolidatedBreakageSummary = invoiceConsolidatedSummeryRepository
//					.findById(consolidatedExpenseDetail.getConsolidatedBreakageSummary().getTripConsolidationID())
//					.orElse(null);
//
//			if (consolidatedBreakageSummary.getApprovedAmount() == null) {
//				consolidatedBreakageSummary.setApprovedAmount(voucher.getTotalAmount());
//				invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
//			} else {
//				BigDecimal modifiedApprovedAmount = consolidatedBreakageSummary.getApprovedAmount()
//						.add(voucher.getTotalAmount());
//				consolidatedBreakageSummary.setApprovedAmount(modifiedApprovedAmount);
//				invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
//			}
//
//		} else {
//			if (listOfConsolidatedExpenseDetails.size() > 1) {
//				listOfConsolidatedExpenseDetails.forEach(consolidationDetails -> {
//
//					consolidationDetails.setApprovedVoucherIds(voucher.getVoucherID().toString());
//					consolidatedExpenseDetailRepository.save(consolidationDetails);
//
//					ConsolidatedBreakageSummary consolidatedBreakageSummary = invoiceConsolidatedSummeryRepository
//							.findById(consolidationDetails.getConsolidatedBreakageSummary().getTripConsolidationID())
//							.orElse(null);
//
//					if (XpendeskConstants.ACCOMMODATION_TYPE
//							.equalsIgnoreCase(consolidatedBreakageSummary.getCategory())) {
//						BigDecimal totalApprovedAmountForAccomodation = voucher.getHotelVoucherID()
//								.getListOfHotelVoucherDetail().stream()
//								.map(hotelVoucherDetail -> hotelVoucherDetail.getAmount()
//										.add(hotelVoucherDetail.getGstAmount()))
//								.reduce(BigDecimal.ZERO, BigDecimal::add);
//
//						consolidatedBreakageSummary.setApprovedAmount(totalApprovedAmountForAccomodation);
//						invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
//
//					} else if (XpendeskConstants.FOOD_TYPE
//							.equalsIgnoreCase(consolidatedBreakageSummary.getCategory())) {
//						BigDecimal totalApprovedAmountForFood = voucher.getHotelVoucherID().getListOfFoodVoucherDetail()
//								.stream()
//								.map(foodVoucherDetail -> foodVoucherDetail.getAmount()
//										.add(foodVoucherDetail.getGstAmount()))
//								.reduce(BigDecimal.ZERO, BigDecimal::add);
//
//						consolidatedBreakageSummary.setApprovedAmount(totalApprovedAmountForFood);
//						invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
//
//					}
//
//				});
//
//			}
//
//		}
//
//	}
//
//	private void updateRejectedVoucherAmountForConsolidationTable(Voucher voucher) {
//
//		List<ConsolidatedExpenseDetail> listOfConsolidatedExpenseDetails = consolidatedExpenseDetailRepository
//				.existByVoucherId(voucher.getVoucherID().toString());
//		if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)) {
//			ConsolidatedExpenseDetail consolidatedExpenseDetail = listOfConsolidatedExpenseDetails.stream().findFirst()
//					.get();
//
//			String rejectedVouchers = String.join(",",
//					Arrays.asList(consolidatedExpenseDetail.getVoucherIds().split(",")).stream()
//							.filter(voucherId -> voucherId.equalsIgnoreCase(voucher.getVoucherID().toString()))
//							.toList());
//
//			if (StringUtils.isBlank(consolidatedExpenseDetail.getRejectedVoucherIds())) {
//				consolidatedExpenseDetail.setRejectedVoucherIds(rejectedVouchers);
//				consolidatedExpenseDetailRepository.save(consolidatedExpenseDetail);
//			} else {
//				String modifiedRejectedVouchers = String.join(",", consolidatedExpenseDetail.getRejectedVoucherIds(),
//						rejectedVouchers);
//				consolidatedExpenseDetail.setApprovedVoucherIds(modifiedRejectedVouchers);
//				consolidatedExpenseDetailRepository.save(consolidatedExpenseDetail);
//			}
//
//			ConsolidatedBreakageSummary consolidatedBreakageSummary = invoiceConsolidatedSummeryRepository
//					.findById(consolidatedExpenseDetail.getConsolidatedBreakageSummary().getTripConsolidationID())
//					.orElse(null);
//
//			if (consolidatedBreakageSummary.getRejectAmount() == null) {
//				consolidatedBreakageSummary.setRejectAmount(voucher.getTotalAmount());
//				invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
//			} else {
//				BigDecimal modifiedRejectedAmount = consolidatedBreakageSummary.getRejectAmount()
//						.add(voucher.getTotalAmount());
//				consolidatedBreakageSummary.setRejectAmount(modifiedRejectedAmount);
//				invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
//			}
//
//		} else if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)) {
//			ConsolidatedExpenseDetail consolidatedExpenseDetail = listOfConsolidatedExpenseDetails.stream().findFirst()
//					.get();
//
//			String rejectedVouchers = String.join(",",
//					Arrays.asList(consolidatedExpenseDetail.getVoucherIds().split(",")).stream()
//							.filter(voucherId -> voucherId.equalsIgnoreCase(voucher.getVoucherID().toString()))
//							.toList());
//
//			if (StringUtils.isBlank(consolidatedExpenseDetail.getRejectedVoucherIds())) {
//				consolidatedExpenseDetail.setRejectedVoucherIds(rejectedVouchers);
//				consolidatedExpenseDetailRepository.save(consolidatedExpenseDetail);
//			} else {
//				String modifiedRejectedVouchers = String.join(",", consolidatedExpenseDetail.getRejectedVoucherIds(),
//						rejectedVouchers);
//				consolidatedExpenseDetail.setApprovedVoucherIds(modifiedRejectedVouchers);
//				consolidatedExpenseDetailRepository.save(consolidatedExpenseDetail);
//			}
//
//			ConsolidatedBreakageSummary consolidatedBreakageSummary = invoiceConsolidatedSummeryRepository
//					.findById(consolidatedExpenseDetail.getConsolidatedBreakageSummary().getTripConsolidationID())
//					.orElse(null);
//
//			if (consolidatedBreakageSummary.getRejectAmount() == null) {
//				consolidatedBreakageSummary.setRejectAmount(voucher.getTotalAmount());
//				invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
//			} else {
//				BigDecimal modifiedRejectedAmount = consolidatedBreakageSummary.getRejectAmount()
//						.add(voucher.getTotalAmount());
//				consolidatedBreakageSummary.setRejectAmount(modifiedRejectedAmount);
//				invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
//			}
//
//		} else {
//			if (listOfConsolidatedExpenseDetails.size() > 1) {
//				listOfConsolidatedExpenseDetails.forEach(consolidationDetails -> {
//
//					consolidationDetails.setRejectedVoucherIds(voucher.getVoucherID().toString());
//					consolidatedExpenseDetailRepository.save(consolidationDetails);
//
//					ConsolidatedBreakageSummary consolidatedBreakageSummary = invoiceConsolidatedSummeryRepository
//							.findById(consolidationDetails.getConsolidatedBreakageSummary().getTripConsolidationID())
//							.orElse(null);
//					if (XpendeskConstants.ACCOMMODATION_TYPE
//							.equalsIgnoreCase(consolidatedBreakageSummary.getCategory())) {
//						BigDecimal totalRejectedAmountForAccomodation = voucher.getHotelVoucherID()
//								.getListOfHotelVoucherDetail().stream()
//								.map(hotelVoucherDetail -> hotelVoucherDetail.getAmount()
//										.add(hotelVoucherDetail.getGstAmount()))
//								.reduce(BigDecimal.ZERO, BigDecimal::add);
//
//						consolidatedBreakageSummary.setRejectAmount(totalRejectedAmountForAccomodation);
//						invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
//
//					} else if (XpendeskConstants.FOOD_TYPE
//							.equalsIgnoreCase(consolidatedBreakageSummary.getCategory())) {
//						BigDecimal totalRejectedAmountForFood = voucher.getHotelVoucherID().getListOfFoodVoucherDetail()
//								.stream()
//								.map(foodVoucherDetail -> foodVoucherDetail.getAmount()
//										.add(foodVoucherDetail.getGstAmount()))
//								.reduce(BigDecimal.ZERO, BigDecimal::add);
//
//						consolidatedBreakageSummary.setRejectAmount(totalRejectedAmountForFood);
//						invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
//
//					}
//
//				});
//
//			}
//		}
//
//	}

	@Override
	public ResponseMassageWithCodeDto partiallyApproval(List<PartApprovalDto> partApprovalDtoList) {

		ResponseMassageWithCodeDto responseMassageWithCodeDto = new ResponseMassageWithCodeDto();

		for (PartApprovalDto partApprovalDto : partApprovalDtoList) {

			partApprovalDto.getListOfVoucherApprovalInfoDtos().forEach(voucherApproval -> {

				Voucher voucher = voucherRepository.findById(voucherApproval.getVoucherId()).orElse(null);

				if (voucher.getIsApproved() == null && voucherApproval.getIsApproved().equalsIgnoreCase("false")) {

					updateRejectedVoucherAmountForConsolidationTable(voucher);

					voucher.setIsApproved(XpendeskConstants.VOUCHER_REJECT);

					voucherRepository.save(voucher);

					responseMassageWithCodeDto.setMassage("Reject Successfully!!");
					responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);

				} else if (voucher.getIsApproved() == null
						&& voucherApproval.getIsApproved().equalsIgnoreCase("true")) {

					updateApprovedAmountAndConsolidationTable(voucher);

					voucher.setIsApproved(XpendeskConstants.VOUCHER_APPROVAL);

					voucherRepository.save(voucher);

					responseMassageWithCodeDto.setMassage("Approved Successfully!!");
					responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);

				} else {
					responseMassageWithCodeDto.setMassage("Already Updated!!");
					responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
				}

			});

		}

		responseMassageWithCodeDto.setMassage("Approved Successfully!!");
		responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);

		return responseMassageWithCodeDto;
	}

	private void updateApprovedAmountAndConsolidationTable(Voucher voucher) {
//		for voucher get the details of all record's of consolidation_details table related
//		to that approved voucher
		List<ConsolidatedExpenseDetail> listOfConsolidatedExpenseDetails = consolidatedExpenseDetailRepository
				.existByVoucherId(voucher.getVoucherID().toString());

		if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)) {
			ConsolidatedExpenseDetail consolidatedExpenseDetail = listOfConsolidatedExpenseDetails.stream().findFirst()
					.get();

			String approvedVouchers = String.join(",",
					Arrays.asList(consolidatedExpenseDetail.getVoucherIds().split(",")).stream()
							.filter(voucherId -> voucherId.equalsIgnoreCase(voucher.getVoucherID().toString()))
							.toList());

			/*
			 * first check if any voucher id present or not in the approverVoucher Id's if
			 * present then fetch the existing record and append new data or else just
			 * normally update the record
			 */
			if (StringUtils.isBlank(consolidatedExpenseDetail.getApprovedVoucherIds())) {
				consolidatedExpenseDetail.setApprovedVoucherIds(approvedVouchers);
				consolidatedExpenseDetailRepository.save(consolidatedExpenseDetail);
			} else {
				String modifiedApprovedVouchers = String.join(",", consolidatedExpenseDetail.getApprovedVoucherIds(),
						approvedVouchers);
				consolidatedExpenseDetail.setApprovedVoucherIds(modifiedApprovedVouchers);
				consolidatedExpenseDetailRepository.save(consolidatedExpenseDetail);
			}

			ConsolidatedBreakageSummary consolidatedBreakageSummary = invoiceConsolidatedSummeryRepository
					.findById(consolidatedExpenseDetail.getConsolidatedBreakageSummary().getTripConsolidationID())
					.orElse(null);

			/*
			 * if the approved amount is already present in the consolidated summary table
			 * then first fetch it then add the another approved amount and then persist in
			 * DB or else if nothing is present in DB then just persist the amount in total
			 * amount.
			 */
			if (consolidatedBreakageSummary.getApprovedAmount() == null) {
				consolidatedBreakageSummary.setApprovedAmount(voucher.getTotalAmount());
				invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
			} else {
				BigDecimal modifiedApprovedAmount = consolidatedBreakageSummary.getApprovedAmount()
						.add(voucher.getTotalAmount());
				consolidatedBreakageSummary.setApprovedAmount(modifiedApprovedAmount);
				invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
			}

		} else if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)) {
			ConsolidatedExpenseDetail consolidatedExpenseDetail = listOfConsolidatedExpenseDetails.stream().findFirst()
					.get();

			String approvedVouchers = String.join(",",
					Arrays.asList(consolidatedExpenseDetail.getVoucherIds().split(",")).stream()
							.filter(voucherId -> voucherId.equalsIgnoreCase(voucher.getVoucherID().toString()))
							.toList());

			if (StringUtils.isBlank(consolidatedExpenseDetail.getApprovedVoucherIds())) {
				consolidatedExpenseDetail.setApprovedVoucherIds(approvedVouchers);
				consolidatedExpenseDetailRepository.save(consolidatedExpenseDetail);
			} else {
				String modifiedApprovedVouchers = String.join(",", consolidatedExpenseDetail.getApprovedVoucherIds(),
						approvedVouchers);
				consolidatedExpenseDetail.setApprovedVoucherIds(modifiedApprovedVouchers);
				consolidatedExpenseDetailRepository.save(consolidatedExpenseDetail);
			}

			ConsolidatedBreakageSummary consolidatedBreakageSummary = invoiceConsolidatedSummeryRepository
					.findById(consolidatedExpenseDetail.getConsolidatedBreakageSummary().getTripConsolidationID())
					.orElse(null);

			if (consolidatedBreakageSummary.getApprovedAmount() == null) {
				consolidatedBreakageSummary.setApprovedAmount(voucher.getTotalAmount());
				invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
			} else {
				BigDecimal modifiedApprovedAmount = consolidatedBreakageSummary.getApprovedAmount()
						.add(voucher.getTotalAmount());
				consolidatedBreakageSummary.setApprovedAmount(modifiedApprovedAmount);
				invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
			}

		} else {
			if (listOfConsolidatedExpenseDetails.size() > 1) {
				listOfConsolidatedExpenseDetails.forEach(consolidationDetails -> {

					consolidationDetails.setApprovedVoucherIds(voucher.getVoucherID().toString());
					consolidatedExpenseDetailRepository.save(consolidationDetails);

					ConsolidatedBreakageSummary consolidatedBreakageSummary = invoiceConsolidatedSummeryRepository
							.findById(consolidationDetails.getConsolidatedBreakageSummary().getTripConsolidationID())
							.orElse(null);

					if (XpendeskConstants.ACCOMMODATION_TYPE
							.equalsIgnoreCase(consolidatedBreakageSummary.getCategory())) {
						BigDecimal totalApprovedAmountForAccomodation = voucher.getHotelVoucherID()
								.getListOfHotelVoucherDetail().stream()
								.map(hotelVoucherDetail -> hotelVoucherDetail.getAmount()
										.add(hotelVoucherDetail.getGstAmount()))
								.reduce(BigDecimal.ZERO, BigDecimal::add);

						consolidatedBreakageSummary.setApprovedAmount(totalApprovedAmountForAccomodation);
						invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);

					} else if (XpendeskConstants.FOOD_TYPE
							.equalsIgnoreCase(consolidatedBreakageSummary.getCategory())) {
						BigDecimal totalApprovedAmountForFood = voucher.getHotelVoucherID().getListOfFoodVoucherDetail()
								.stream()
								.map(foodVoucherDetail -> foodVoucherDetail.getAmount()
										.add(foodVoucherDetail.getGstAmount()))
								.reduce(BigDecimal.ZERO, BigDecimal::add);

						consolidatedBreakageSummary.setApprovedAmount(totalApprovedAmountForFood);
						invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);

					}

				});

			}

		}

	}

	private void updateRejectedVoucherAmountForConsolidationTable(Voucher voucher) {

//		for voucher get the details of all record's of consolidation_details table related
//		to that rejected voucher
		List<ConsolidatedExpenseDetail> listOfConsolidatedExpenseDetails = consolidatedExpenseDetailRepository
				.existByVoucherId(voucher.getVoucherID().toString());
		if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)) {
			ConsolidatedExpenseDetail consolidatedExpenseDetail = listOfConsolidatedExpenseDetails.stream().findFirst()
					.get();

			String rejectedVouchers = String.join(",",
					Arrays.asList(consolidatedExpenseDetail.getVoucherIds().split(",")).stream()
							.filter(voucherId -> voucherId.equalsIgnoreCase(voucher.getVoucherID().toString()))
							.toList());

			/*
			 * first check if any voucher id present or not in the rejectedVouchers Id's if
			 * present then fetch the existing record and append new data or else just
			 * normally update the record
			 */
			if (StringUtils.isBlank(consolidatedExpenseDetail.getRejectedVoucherIds())) {
				consolidatedExpenseDetail.setRejectedVoucherIds(rejectedVouchers);
				consolidatedExpenseDetailRepository.save(consolidatedExpenseDetail);
			} else {
				String modifiedRejectedVouchers = String.join(",", consolidatedExpenseDetail.getRejectedVoucherIds(),
						rejectedVouchers);
				consolidatedExpenseDetail.setApprovedVoucherIds(modifiedRejectedVouchers);
				consolidatedExpenseDetailRepository.save(consolidatedExpenseDetail);
			}

			ConsolidatedBreakageSummary consolidatedBreakageSummary = invoiceConsolidatedSummeryRepository
					.findById(consolidatedExpenseDetail.getConsolidatedBreakageSummary().getTripConsolidationID())
					.orElse(null);

			/*
			 * if the rejected amount is already present in the consolidated summary table
			 * then first fetch it then add the another rejected amount and then persist in
			 * DB or else if nothing is present in DB then just persist the amount in total
			 * amount.
			 */
			if (consolidatedBreakageSummary.getRejectAmount() == null) {
				consolidatedBreakageSummary.setRejectAmount(voucher.getTotalAmount());
				invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
			} else {
				BigDecimal modifiedRejectedAmount = consolidatedBreakageSummary.getRejectAmount()
						.add(voucher.getTotalAmount());
				consolidatedBreakageSummary.setRejectAmount(modifiedRejectedAmount);
				invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
			}

		} else if (voucher.getInvoiceType().equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)) {
			ConsolidatedExpenseDetail consolidatedExpenseDetail = listOfConsolidatedExpenseDetails.stream().findFirst()
					.get();

			String rejectedVouchers = String.join(",",
					Arrays.asList(consolidatedExpenseDetail.getVoucherIds().split(",")).stream()
							.filter(voucherId -> voucherId.equalsIgnoreCase(voucher.getVoucherID().toString()))
							.toList());

			if (StringUtils.isBlank(consolidatedExpenseDetail.getRejectedVoucherIds())) {
				consolidatedExpenseDetail.setRejectedVoucherIds(rejectedVouchers);
				consolidatedExpenseDetailRepository.save(consolidatedExpenseDetail);
			} else {
				String modifiedRejectedVouchers = String.join(",", consolidatedExpenseDetail.getRejectedVoucherIds(),
						rejectedVouchers);
				consolidatedExpenseDetail.setApprovedVoucherIds(modifiedRejectedVouchers);
				consolidatedExpenseDetailRepository.save(consolidatedExpenseDetail);
			}

			ConsolidatedBreakageSummary consolidatedBreakageSummary = invoiceConsolidatedSummeryRepository
					.findById(consolidatedExpenseDetail.getConsolidatedBreakageSummary().getTripConsolidationID())
					.orElse(null);

			if (consolidatedBreakageSummary.getRejectAmount() == null) {
				consolidatedBreakageSummary.setRejectAmount(voucher.getTotalAmount());
				invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
			} else {
				BigDecimal modifiedRejectedAmount = consolidatedBreakageSummary.getRejectAmount()
						.add(voucher.getTotalAmount());
				consolidatedBreakageSummary.setRejectAmount(modifiedRejectedAmount);
				invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);
			}

		} else {
			if (listOfConsolidatedExpenseDetails.size() > 1) {
				listOfConsolidatedExpenseDetails.forEach(consolidationDetails -> {

					consolidationDetails.setRejectedVoucherIds(voucher.getVoucherID().toString());
					consolidatedExpenseDetailRepository.save(consolidationDetails);

					ConsolidatedBreakageSummary consolidatedBreakageSummary = invoiceConsolidatedSummeryRepository
							.findById(consolidationDetails.getConsolidatedBreakageSummary().getTripConsolidationID())
							.orElse(null);
					if (XpendeskConstants.ACCOMMODATION_TYPE
							.equalsIgnoreCase(consolidatedBreakageSummary.getCategory())) {
						BigDecimal totalRejectedAmountForAccomodation = voucher.getHotelVoucherID()
								.getListOfHotelVoucherDetail().stream()
								.map(hotelVoucherDetail -> hotelVoucherDetail.getAmount()
										.add(hotelVoucherDetail.getGstAmount()))
								.reduce(BigDecimal.ZERO, BigDecimal::add);

						consolidatedBreakageSummary.setRejectAmount(totalRejectedAmountForAccomodation);
						invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);

					} else if (XpendeskConstants.FOOD_TYPE
							.equalsIgnoreCase(consolidatedBreakageSummary.getCategory())) {
						BigDecimal totalRejectedAmountForFood = voucher.getHotelVoucherID().getListOfFoodVoucherDetail()
								.stream()
								.map(foodVoucherDetail -> foodVoucherDetail.getAmount()
										.add(foodVoucherDetail.getGstAmount()))
								.reduce(BigDecimal.ZERO, BigDecimal::add);

						consolidatedBreakageSummary.setRejectAmount(totalRejectedAmountForFood);
						invoiceConsolidatedSummeryRepository.save(consolidatedBreakageSummary);

					}

				});

			}
		}

	}

	@Override
	public ResponseMassageWithCodeDto tripConfirmation(WorkflowTaskDto workflowTaskDto) {

		ResponseMassageWithCodeDto responseMassageWithCodeDto = new ResponseMassageWithCodeDto();

		if (org.apache.commons.lang3.StringUtils.containsAnyIgnoreCase(workflowTaskDto.getActionType(), "A")) {
			workflowTaskDto.setActionType(XpendeskConstants.APPROVED);
			System.out.println(workflowTaskDto.getReferenceID());
			workflowServiceImpl.processNextWorkflowTask(workflowTaskDto);
			saveApprovalAmountInDb(workflowTaskDto,workflowTaskDto.getReferenceID());

			responseMassageWithCodeDto.setMassage(XpendeskConstants.APPROVED);
			responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
		} else {
			workflowTaskDto.setActionType(XpendeskConstants.REJECTED);
			workflowServiceImpl.processNextWorkflowTask(workflowTaskDto);

			responseMassageWithCodeDto.setMassage(XpendeskConstants.REJECTED);
			responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
		}

		/*
		 * Trip trip =
		 * tripRepository.findById(workflowTaskDto.getReferenceID()).orElse(null);
		 * List<Voucher> listOfVoucher = voucherRepository.findByTrip(trip); Boolean
		 * matchAllIsAproved = listOfVoucher.stream().allMatch(voucher ->
		 * voucher.getIsApproved() != null &&
		 * voucher.getIsApproved().equalsIgnoreCase(XpendeskConstants.VOUCHER_APPROVAL))
		 * ;
		 * 
		 * Boolean matchAllIsReject = listOfVoucher.stream().allMatch(voucher ->
		 * voucher.getIsApproved() != null &&
		 * voucher.getIsApproved().equalsIgnoreCase(XpendeskConstants.VOUCHER_REJECT));
		 * 
		 * if (matchAllIsAproved) {
		 * workflowTaskDto.setActionType(XpendeskConstants.APPROVED);
		 * workflowServiceImpl.processNextWorkflowTask(workflowTaskDto);
		 * 
		 * // New Implementation At 20-11-2024 saveApprovalAmountInDb(workflowTaskDto);
		 * 
		 * responseMassageWithCodeDto.setMassage(XpendeskConstants.APPROVED);
		 * responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
		 * 
		 * } else if (matchAllIsReject) {
		 * workflowTaskDto.setActionType(XpendeskConstants.REJECTED);
		 * workflowServiceImpl.processNextWorkflowTask(workflowTaskDto);
		 * 
		 * responseMassageWithCodeDto.setMassage(XpendeskConstants.REJECTED);
		 * responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
		 * 
		 * } else { workflowTaskDto.setActionType(XpendeskConstants.APPROVED);
		 * workflowServiceImpl.processNextWorkflowTask(workflowTaskDto);
		 * 
		 * responseMassageWithCodeDto.setMassage(XpendeskConstants.PART_APPROVAL);
		 * responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
		 * 
		 * }
		 */
		return responseMassageWithCodeDto;
	}

	private void saveApprovalAmountInDb(WorkflowTaskDto workflowTaskDto,int referenceId) {

		String typeOfReference = workflowTaskDto.getReferenceType();
		//BigDecimal totalApprovedAmount = 0;
		//BigDecimal totalApprovedAmount = new BigDecimal("0");
		//int totalApprovedAmount = 0;
		//BigDecimal totalApprovedAmount;
		BigDecimal[] totalApprovedAmount = { BigDecimal.ZERO };



		List<ConsolidatedBreakageSummary> listOfConsBrkSummary = new ArrayList<ConsolidatedBreakageSummary>();

		try {
			if (org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase(typeOfReference, "T")) {
				workflowTaskDto.getConsidWiseApproveAmmount().forEach(eachConsAmount -> {
					invoiceConsolidatedSummeryRepository.findById(eachConsAmount.getTripConsolidationId())
							.ifPresent(consElement -> {
								consElement.setApprovedAmount(eachConsAmount.getApprovedAmount());
								listOfConsBrkSummary.add(consElement);
								totalApprovedAmount[0] = totalApprovedAmount[0].add(eachConsAmount.getApprovedAmount());
								//System.out.println("Total approved amount"+totalApprovedAmount[0]);
							});
				});
				if (!listOfConsBrkSummary.isEmpty())
					invoiceConsolidatedSummeryRepository.saveAll(listOfConsBrkSummary);
					TripApprovedAmount tripApprovedAmount = new TripApprovedAmount();
					tripApprovedAmount.setTripId(referenceId);
					tripApprovedAmount.setApprovedAmount(totalApprovedAmount[0]);
					tripApprovedAmountRepository.save(tripApprovedAmount);
			        //System.out.println("Saved total approved amount into TripApprovedAmount table");
					
					//tripApprovedAmountRepository.save(null)
			} else {
				voucherRepository.findById(workflowTaskDto.getReferenceID()).ifPresent(savedVoucher -> {
					savedVoucher.setApprovedAmount(workflowTaskDto.getApprovedAmount());
					voucherRepository.save(savedVoucher);
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
