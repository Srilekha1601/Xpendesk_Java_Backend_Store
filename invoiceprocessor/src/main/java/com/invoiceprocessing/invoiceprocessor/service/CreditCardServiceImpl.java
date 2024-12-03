package com.invoiceprocessing.invoiceprocessor.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.GlobalExceptionHandler.ResourceNotFoudException;
import com.invoiceprocessing.invoiceprocessor.mapper.AllVoucherMapperForSingleExpenseImpl;
import com.invoiceprocessing.invoiceprocessor.mapper.CreditCardMapperImpl;
import com.invoiceprocessing.invoiceprocessor.mapper.SingleVoucherMapperForConveyanceImpl;
import com.invoiceprocessing.invoiceprocessor.mapper.SingleVoucherMapperForFoodImpl;
import com.invoiceprocessing.invoiceprocessor.model.CreditCardTransactions;
import com.invoiceprocessing.invoiceprocessor.model.Employee;
import com.invoiceprocessing.invoiceprocessor.model.Trip;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.repository.ConveyanceVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.CreditCardTransactionsRepository;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.FoodVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.HotelVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherRepository;
import com.invoiceprocessing.invoiceprocessor.response.CreditCardTransactionsDto;
import com.invoiceprocessing.invoiceprocessor.response.EmployeeDto;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.CreditCardService;
import com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants;

@Service
public class CreditCardServiceImpl implements CreditCardService {

	@Autowired
	CreditCardTransactionsRepository creditCardTransactionsRepository;

	@Autowired
	TripRepository tripRepository;

	@Autowired
	CreditCardMapperImpl creditCardMapperImpl;

	@Autowired
	SingleVoucherMapperForFoodImpl singleVoucherMapperForFoodImpl;

	@Autowired
	SingleVoucherMapperForConveyanceImpl singleVoucherMapperForConveyanceImpl;

	@Autowired
	AllVoucherMapperForSingleExpenseImpl allVoucherMapperForSingleExpenseImpl;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	FoodVoucherRepository foodVoucherRepository;

	@Autowired
	ConveyanceVoucherRepository conveyanceVoucherRepository;

	@Autowired
	HotelVoucherRepository hotelVocuherRepository;

	@Override
	public List<CreditCardTransactionsDto> getTransactions(Integer employeeId) {
		// TODO Auto-generated method stub

		Employee employee = employeeRepository.findById(employeeId).orElse(null);

		CreditCardTransactionsDto creditCardTransactionsDto = null;

		List<CreditCardTransactionsDto> listOfCardTransactionsDtos = new ArrayList<CreditCardTransactionsDto>();

		List<CreditCardTransactions> listOfCardTransactions = creditCardTransactionsRepository
				.findAllByEmployee(employee).stream()
				.filter(ccTransaction -> ccTransaction.getTransactionStatus().equalsIgnoreCase("N")
						&& ccTransaction.getExpenseType() == null)
				.toList();

		for (CreditCardTransactions creditCardTransactions : listOfCardTransactions) {
			creditCardTransactionsDto = new CreditCardTransactionsDto();

			creditCardTransactionsDto.setCcTransactionId(creditCardTransactions.getCcTransactionId().toString());
			creditCardTransactionsDto.setTransactionNo(creditCardTransactions.getTransactionNo());
			creditCardTransactionsDto.setEmployeeId(creditCardTransactions.getEmployee().getEmployeeCode());
			creditCardTransactionsDto.setMerchantName(creditCardTransactions.getMerchantName());
			creditCardTransactionsDto
					.setTransactionDateTime(creditCardTransactions.getTransactionDateTime().toString());
			creditCardTransactionsDto.setTransactionStatus(creditCardTransactions.getTransactionStatus());
			creditCardTransactionsDto.setTransactionAmount(creditCardTransactions.getTransactionAmount().toString());

			listOfCardTransactionsDtos.add(creditCardTransactionsDto);

		}

		return listOfCardTransactionsDtos;
	}

	@Override
	public ResponseMassageWithCodeDto saveVoucherForCCTransaction(List<VoucherWithOutTripDto> listVoucherDto) {

		ResponseMassageWithCodeDto responseMassageWithCodeDto = new ResponseMassageWithCodeDto();

		VoucherWithOutTripDto voucherWithOutTripDto = listVoucherDto.stream().findFirst().get();

		Voucher voucher = voucherRepository
				.save(allVoucherMapperForSingleExpenseImpl.convertSingleVoucherDtoToVoucher(voucherWithOutTripDto));
		if (voucherWithOutTripDto.getBillType().equalsIgnoreCase(XpendeskConstants.FOOD_TYPE)) {

			voucherWithOutTripDto.setVoucherID(voucher.getVoucherID().toString());
			foodVoucherRepository
					.save(singleVoucherMapperForFoodImpl.singleVoucherMapperToSingleExpense(voucherWithOutTripDto));
			updateTransactionStatus(voucherWithOutTripDto);

		} else if (voucherWithOutTripDto.getBillType().equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)) {
			voucherWithOutTripDto.setVoucherID(voucher.getVoucherID().toString());
			conveyanceVoucherRepository.save(singleVoucherMapperForConveyanceImpl
					.convertConveyanceVoucherDtoToConveyanceVoucher(voucherWithOutTripDto));
			updateTransactionStatus(voucherWithOutTripDto);
		}

		responseMassageWithCodeDto.setMassage("saved");
		responseMassageWithCodeDto.setStatusCode(HttpStatus.SC_OK);
		return responseMassageWithCodeDto;

	}

	private void updateTransactionStatus(VoucherWithOutTripDto voucherWithOutTripDto) {
		CreditCardTransactions creditCardTransactions = creditCardTransactionsRepository
				.findById(Integer.parseInt(voucherWithOutTripDto.getCcTransactionId())).get();
		creditCardTransactions.setTransactionStatus("Y");
		creditCardTransactionsRepository.save(creditCardTransactions);
	}

	@Override
	public List<CreditCardTransactionsDto> confirmTransaction(EmployeeDto employeeDto) {

		Employee employee = employeeRepository.findById(employeeDto.getEmployeeId()).get();

		CreditCardTransactionsDto creditCardTransactionsDto = null;

		List<CreditCardTransactionsDto> listCardTransactionsDtos = new ArrayList<>();

		List<CreditCardTransactions> listCardTransactions = creditCardTransactionsRepository.findAllByEmployee(employee)
				.stream().filter(ccTransaction -> ccTransaction.getTransactionStatus().equalsIgnoreCase("Y")
						&& ccTransaction.getExpenseType() == null)
				.toList();
		for (CreditCardTransactions creditCardTransactions : listCardTransactions) {

			creditCardTransactionsDto = creditCardMapperImpl.getCreditCardTransactionToDto(creditCardTransactions);
			creditCardTransactionsDto.setVoucherId(
					voucherRepository.findByCreditCardTransactionsOrderByVoucherIDDesc(creditCardTransactions).stream()
							.findFirst().get().getVoucherID().toString());

			listCardTransactionsDtos.add(creditCardTransactionsDto);

		}

		return listCardTransactionsDtos;

	}

	@Override
	public ResponseMassageWithCodeDto updateVoucherIfSingleExpense(VoucherDto voucherDto) {

		ResponseMassageWithCodeDto messageCodeDto = new ResponseMassageWithCodeDto();

		CreditCardTransactions creditCardTransactions = creditCardTransactionsRepository
				.findById(Integer.parseInt(voucherDto.getCcTransactionId())).get();

		creditCardTransactions.setExpenseType("V");

		creditCardTransactionsRepository.save(creditCardTransactions);

		Voucher voucher = voucherRepository.findById(Integer.parseInt(voucherDto.getVoucherID())).orElse(null);

		voucher.setIsSingleExpense("Y");

		voucherRepository.save(voucher);

		messageCodeDto.setMassage("Update Complete");
		messageCodeDto.setStatusCode(HttpStatus.SC_OK);

		return messageCodeDto;

	}

	@Override
	public ResponseMassageWithCodeDto addVoucherInTrip(VoucherDto voucherDto) {

		ResponseMassageWithCodeDto messageCodeDto = new ResponseMassageWithCodeDto();

		CreditCardTransactions creditCardTransactions = creditCardTransactionsRepository
				.findById(Integer.parseInt(voucherDto.getCcTransactionId()))
				.orElseThrow(() -> new ResourceNotFoudException());

		creditCardTransactions.setExpenseType("T");
		creditCardTransactions.setTransactionStatus("Y");
		creditCardTransactionsRepository.save(creditCardTransactions);

		Trip trip = tripRepository.findById(Integer.parseInt(voucherDto.getTripId())).get();

		Voucher voucher = voucherRepository.findById(Integer.parseInt(voucherDto.getVoucherID())).orElse(null);

		voucher.setTrip(trip);
		voucher.setIsSingleExpense("N");

		voucherRepository.save(voucher);

		messageCodeDto.setMassage("voucher added in trip successfully!!");
		messageCodeDto.setStatusCode(HttpStatus.SC_OK);

		return messageCodeDto;
	}

}
