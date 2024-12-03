package com.invoiceprocessing.invoiceprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:Xpendesk.properties")
public class InvoiceprocessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoiceprocessorApplication.class, args);
	}

}
