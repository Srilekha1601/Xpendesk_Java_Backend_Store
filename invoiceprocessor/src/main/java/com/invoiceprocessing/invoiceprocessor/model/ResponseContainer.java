package com.invoiceprocessing.invoiceprocessor.model;

//import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseContainer {

	@JsonProperty("headers")
	private Map<String, Object> headers;
	@JsonProperty("body")
	private String body;
	@JsonProperty("statusCode")
	private String statusCode;
	@JsonProperty("statusCodeValue")
	private int statusCodeValue;

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCodeValue() {
		return statusCodeValue;
	}

	public void setStatusCodeValue(int statusCodeValue) {
		this.statusCodeValue = statusCodeValue;
	}

}
