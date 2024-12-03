package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import com.invoiceprocessing.invoiceprocessor.response.MailBeanDto;

public interface EmailService {

	public void sendEmail(MailBeanDto mailBean);

}
