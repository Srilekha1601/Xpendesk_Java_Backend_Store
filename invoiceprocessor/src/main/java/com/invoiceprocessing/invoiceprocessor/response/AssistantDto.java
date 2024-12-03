package com.invoiceprocessing.invoiceprocessor.response;

import java.util.List;

import lombok.Data;

@Data
public class AssistantDto {

	private String mapperId;

	private String managerId;

	private String assistantId;

	private String assistantName;

	private String assistantAccess;

	private String assistantAccessExpiryDate;

	private String accessPermissionTimePeriod;

	private List<AssignUserInfoDto> listOfAssignUserInfoDtos;

}
