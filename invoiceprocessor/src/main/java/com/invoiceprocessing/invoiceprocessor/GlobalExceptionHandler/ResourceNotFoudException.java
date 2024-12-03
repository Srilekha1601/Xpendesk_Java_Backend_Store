package com.invoiceprocessing.invoiceprocessor.GlobalExceptionHandler;

public class ResourceNotFoudException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoudException(String message) {
		super(message);
	}

	public ResourceNotFoudException() {
		super("resource not present in database !!!");
	}

}
