package com.invoiceprocessing.invoiceprocessor.response;

import lombok.Data;

@Data
public class MailBeanDto {

	private String from;

	private String password;

	private String to;

	private String cc;

	private String subject;

	private String body;

}
