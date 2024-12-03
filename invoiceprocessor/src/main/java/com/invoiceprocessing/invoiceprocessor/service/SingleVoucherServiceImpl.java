package com.invoiceprocessing.invoiceprocessor.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.mapper.ConsolidatedVoucherMapperImpl;
import com.invoiceprocessing.invoiceprocessor.mapper.VoucherWithOutTripMapper;
import com.invoiceprocessing.invoiceprocessor.model.VoucherEntity;
import com.invoiceprocessing.invoiceprocessor.response.SingleVoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.SingleExpenseService;
import com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants;

@Service
public class SingleVoucherServiceImpl implements SingleExpenseService {

	@Value("${xpendesk.db.url}")
	private String url;

	@Value("${xpendesk.db.user}")
	private String user;

	@Value("${xpendesk.db.password}")
	private String password;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	ConsolidatedVoucherMapperImpl consolidatedVoucherMapperImpl;

	@Override
	public List<SingleVoucherDto> singleVoucherSave(List<VoucherWithOutTripDto> voucherDtos) {
		// TODO Auto-generated method stub
		List<SingleVoucherDto> listOfSingleVouchersDto = null;
		for (VoucherWithOutTripDto voucher : voucherDtos) {
			@SuppressWarnings("unchecked")
			VoucherWithOutTripMapper<VoucherWithOutTripDto, VoucherEntity> foodVoucherMapperSingleExpn = (VoucherWithOutTripMapper<VoucherWithOutTripDto, VoucherEntity>) applicationContext
					.getBean(getExpenseType(voucher.getBillType().toLowerCase()));
			foodVoucherMapperSingleExpn.dtoToModelWithOutTrip(voucher);

			if (voucher.getIsSingleExpense().equalsIgnoreCase("Y")
					&& voucher.getBillType().equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)) {
				listOfSingleVouchersDto = consolidatedVoucherMapperImpl
						.singleExpenseForFood(voucher.getIsSingleExpense());

			} else if (voucher.getIsSingleExpense().equalsIgnoreCase("Y")
					&& voucher.getBillType().equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)) {
				listOfSingleVouchersDto = consolidatedVoucherMapperImpl
						.singleExpenseForConveyance(voucher.getIsSingleExpense());

			} else if (voucher.getIsSingleExpense().equalsIgnoreCase("Y")
					&& voucher.getBillType().equalsIgnoreCase(XpendeskConstants.HOTEL_TYPE)) {
				// call the service for Hotel Voucher...
			}
		}
		return listOfSingleVouchersDto;
	}

	private String getExpenseType(String expenseType) {
		return expenseType + "ForSingleExpense";
	}

	public List<VoucherDto> getVoucherInfoWithRespectToEmployeeId(Integer referenceId) {

		String query = "SELECT \r\n" + "    v.VOUCHER_ID, \r\n" + "    v.CLAIMED_AMOUNT, \r\n"
				+ "    v.TOTAL_AMOUNT, \r\n" + "    v.MERCHANT_NAME, \r\n" + "    v.FILE_IMAGE, \r\n"
				+ "    v.FILE_NAME, \r\n" + "    v.INVOICE_DATE, \r\n" + "    v.INVOICE_TYPE,\r\n"
				+ "    v.INVOICE_NO,\r\n" + "    fv.CGST_AMOUNT, \r\n" + "    fv.SGST_AMOUNT,\r\n"
				+ "    fv.CONTAINS_LIQUOR,\r\n" + "    cv.DISTANCE, \r\n" + "    cv.FROM_LOCATION, \r\n"
				+ "    cv.TRAVEL_CLASS,\r\n" + "    cv.MODE_OF_TRAVEL,\r\n" + "    cv.TO_LOCATION,\r\n"
				+ "    cv.VEHICLE_TYPE,\r\n" + "    cv.ARRIVAL_DATE,\r\n" + "    cv.DEPARTURE_DATE,\r\n"
				+ "    cv.ARRIVAL_TIME,\r\n" + "    cv.DEPARTURE_TIME,\r\n" + "    cv.INTRA_INTER_CITY\r\n"
				+ "FROM \r\n" + "    voucher v \r\n" + "LEFT JOIN \r\n"
				+ "    food_voucher fv ON v.VOUCHER_ID = fv.VOUCHER_ID\r\n" + "LEFT JOIN \r\n"
				+ "    conveyance_voucher cv ON v.VOUCHER_ID = cv.VOUCHER_ID\r\n" + "-- LEFT JOIN\r\n"
				+ "-- 	workflow_task wt ON wt.reference_id = v.VOUCHER_ID\r\n" + "WHERE \r\n"
				+ "	v.VOUCHER_ID = ?\r\n" + "    AND v.SINGLE_EXPENSE = 'Y';";

		VoucherDto voucherDto = null;
		List<VoucherDto> listOfVoucherDtos = new ArrayList<VoucherDto>();

		try (
				// Establish a connection
				Connection connection = DriverManager.getConnection(url, user, password);

				// Create a prepared statement
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			// Set the parameter value
			preparedStatement.setInt(1, referenceId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				while (resultSet.next()) {
					voucherDto = new VoucherDto();
					voucherDto.setVoucherID(resultSet.getString("VOUCHER_ID"));
					voucherDto.setClaimedAmount(resultSet.getString("CLAIMED_AMOUNT"));
					voucherDto.setTotalAmount(resultSet.getString("TOTAL_AMOUNT"));
					voucherDto.setBillType(resultSet.getString("INVOICE_TYPE"));
					voucherDto.setMerchantName(resultSet.getString("MERCHANT_NAME"));
					voucherDto.setFilename(resultSet.getString("FILE_NAME"));
					voucherDto.setImage(resultSet.getBytes("FILE_IMAGE"));
					voucherDto.setDate(resultSet.getDate("INVOICE_DATE") == null
							? new SimpleDateFormat("dd-MM-yyyy").format(new Date())
							: new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("INVOICE_DATE")));
					voucherDto.setInvoiceNo(resultSet.getString("INVOICE_NO"));
					if (resultSet.getString("INVOICE_TYPE").equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)) {
						voucherDto.setCgstAmount(resultSet.getString("CGST_AMOUNT") == null ? "0.0"
								: resultSet.getString("CGST_AMOUNT"));
						voucherDto.setSgstAmount(resultSet.getString("SGST_AMOUNT") == null ? "0.0"
								: resultSet.getString("SGST_AMOUNT"));
						voucherDto.setLiquorStatus(resultSet.getString("CONTAINS_LIQUOR"));
					} else {
						voucherDto.setNoOfKm(resultSet.getString("DISTANCE"));
						voucherDto.setFromLocation(resultSet.getString("FROM_LOCATION"));
						voucherDto.setTravelClass(resultSet.getString("TRAVEL_CLASS") == null ? "Not Present"
								: resultSet.getString("TRAVEL_CLASS"));
						voucherDto.setModeOfTravel(resultSet.getString("MODE_OF_TRAVEL"));
						voucherDto.setToLocation(resultSet.getString("TO_LOCATION"));
						voucherDto.setArrivalDate(
								new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("ARRIVAL_DATE")));
						voucherDto.setArrivalTime(resultSet.getString("ARRIVAL_TIME") == null ? "Not Present"
								: resultSet.getString("ARRIVAL_TIME"));
						voucherDto.setDepartureDate(
								new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("DEPARTURE_DATE")));
						voucherDto.setDepartureTime(resultSet.getString("DEPARTURE_TIME") == null ? "Not Present"
								: resultSet.getString("DEPARTURE_TIME"));
						voucherDto.setInterIntraCity(resultSet.getString("INTRA_INTER_CITY"));
					}
					listOfVoucherDtos.add(voucherDto);

				}

			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return listOfVoucherDtos;
	}

}
