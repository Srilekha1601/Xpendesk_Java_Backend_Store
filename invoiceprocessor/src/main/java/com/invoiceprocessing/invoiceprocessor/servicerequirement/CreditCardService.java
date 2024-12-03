package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.response.CreditCardTransactionsDto;
import com.invoiceprocessing.invoiceprocessor.response.EmployeeDto;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;

public interface CreditCardService {

	public List<CreditCardTransactionsDto> getTransactions(Integer employeeId);

	public ResponseMassageWithCodeDto saveVoucherForCCTransaction(List<VoucherWithOutTripDto> listVoucherDto);
	
	public List<CreditCardTransactionsDto> confirmTransaction(EmployeeDto employeeDto);
	
	public ResponseMassageWithCodeDto updateVoucherIfSingleExpense(VoucherDto voucherDto);
	
	public ResponseMassageWithCodeDto addVoucherInTrip(VoucherDto voucherDto);
	
}
