package com.invoiceprocessing.invoiceprocessor.payloadbody;

import lombok.Data;

@Data
public class UploadPdf {

	private byte[] pdf;

	private String type;

}
