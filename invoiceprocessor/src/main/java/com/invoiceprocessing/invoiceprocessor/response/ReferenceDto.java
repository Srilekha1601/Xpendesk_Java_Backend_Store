package com.invoiceprocessing.invoiceprocessor.response;

import com.invoiceprocessing.invoiceprocessor.model.Employee;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReferenceDto {

	Integer referenceId;
	String referenceType;
	Employee employee;

}
