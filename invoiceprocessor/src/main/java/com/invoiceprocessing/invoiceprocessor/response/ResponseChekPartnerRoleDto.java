package com.invoiceprocessing.invoiceprocessor.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseChekPartnerRoleDto {

	private String message;

	private Boolean isPartner;

	private Integer status;

}
