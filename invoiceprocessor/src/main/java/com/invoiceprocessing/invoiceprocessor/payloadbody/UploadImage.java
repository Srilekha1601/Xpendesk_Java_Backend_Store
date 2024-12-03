package com.invoiceprocessing.invoiceprocessor.payloadbody;

import lombok.Data;

@Data
public class UploadImage {

	private byte[] image;

	private String type;

}