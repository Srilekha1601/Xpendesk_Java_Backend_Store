package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.response.SingleVoucherDto;

public interface SingleExpenseHistoryService {

	List<SingleVoucherDto> singleExpenseHistroyService(Integer employeeId);
}
