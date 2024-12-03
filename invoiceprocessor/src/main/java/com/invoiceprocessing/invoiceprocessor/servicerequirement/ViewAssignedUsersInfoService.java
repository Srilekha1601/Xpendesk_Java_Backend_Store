package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.response.AssignUserInfoDto;

public interface ViewAssignedUsersInfoService {

	public List<AssignUserInfoDto> viewAssignedUsersInfo(AssignUserInfoDto assignUserInfo);

}
