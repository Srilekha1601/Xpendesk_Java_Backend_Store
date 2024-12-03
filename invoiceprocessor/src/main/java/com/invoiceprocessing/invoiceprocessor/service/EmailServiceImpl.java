package com.invoiceprocessing.invoiceprocessor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.response.MailBeanDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public void sendEmail(MailBeanDto mailBean) {
		SimpleMailMessage massage = new SimpleMailMessage();

		massage.setFrom(mailBean.getFrom());
		massage.setTo("anindita@elogixmail.com");
		massage.setCc("anirban@elogixmail.com");
		massage.setText(mailBean.getBody());
		massage.setSubject("Query Mail");

		javaMailSender.send(massage);
		System.out.println("mail send...");
	}

}
