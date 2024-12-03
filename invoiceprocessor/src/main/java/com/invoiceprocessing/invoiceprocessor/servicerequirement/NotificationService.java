package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;

import com.invoiceprocessing.invoiceprocessor.response.NotificationDto;

public interface NotificationService {

	public List<NotificationDto> getNotification(NotificationDto notificationDto);

}
