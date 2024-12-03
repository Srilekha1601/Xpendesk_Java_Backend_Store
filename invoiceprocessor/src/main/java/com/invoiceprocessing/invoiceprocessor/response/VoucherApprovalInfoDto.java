package com.invoiceprocessing.invoiceprocessor.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherApprovalInfoDto {

	private Integer voucherId;

	private String isApproved;

}