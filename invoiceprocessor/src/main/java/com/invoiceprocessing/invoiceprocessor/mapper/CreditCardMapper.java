package com.invoiceprocessing.invoiceprocessor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.invoiceprocessing.invoiceprocessor.model.CreditCardTransactions;
import com.invoiceprocessing.invoiceprocessor.response.CreditCardTransactionsDto;

@Mapper(componentModel = "spring")
public abstract class CreditCardMapper {

	@Mapping(target = "employeeId", source = "creditCardTransactions", qualifiedByName = "getEmployeeIdByEmployee")
	@Mapping(target = "voucherId", ignore = true)
	public abstract CreditCardTransactionsDto getCreditCardTransactionToDto(
			CreditCardTransactions creditCardTransactions);

	@Named("getEmployeeIdByEmployee")
	protected String getEmployeeIdByEmployee(CreditCardTransactions creditCardTransactions) {
		return creditCardTransactions.getEmployee().getEmployeeId().toString();
	}

}
