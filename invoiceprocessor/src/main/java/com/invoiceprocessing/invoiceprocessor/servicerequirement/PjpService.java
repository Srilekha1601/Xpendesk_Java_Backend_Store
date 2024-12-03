package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import com.invoiceprocessing.invoiceprocessor.response.PjpEmployeeDto;
import com.invoiceprocessing.invoiceprocessor.response.ResponseMassageWithCodeDto;

public interface PjpService {

	public ResponseMassageWithCodeDto savePjp(PjpEmployeeDto pjpEmployeeDto);

	public PjpEmployeeDto getPjp(PjpEmployeeDto pjpEmployeeDto);

}
