package com.invoiceprocessing.invoiceprocessor.model;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class ErrorHandle extends Throwable {

	@JsonProperty("massage")
	private byte[] massage;

	public byte[] getMassage() {
		return massage;
	}

	public void setMassage(byte[] massage) {
		this.massage = massage;
	}

}
