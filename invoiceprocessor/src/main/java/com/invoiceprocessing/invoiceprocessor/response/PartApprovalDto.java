package com.invoiceprocessing.invoiceprocessor.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartApprovalDto {

	private Integer consolidationId;

	private List<VoucherApprovalInfoDto> listOfVoucherApprovalInfoDtos;

}