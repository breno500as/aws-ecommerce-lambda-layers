package com.br.aws.ecommerce.layers.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum InvoiceTranscationStatus {
	
	GENERATED("URL_GENERATED"),
	RECEIVED("INVOICE_RECEIVED"),
	PROCESSED("INVOICE_PROCESSED"),
	TIMEOUT("TIMEOUT"),
	CANCELLED("INVOICE_CANCELLED"),
	NON_VALID_INVOICE_NUMBER("NON_VALID_INVOICE_NUMBER");
	
	private String value;

	private InvoiceTranscationStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	@JsonCreator
	public static InvoiceTranscationStatus forValue(String value) {
		return Arrays.asList(InvoiceTranscationStatus.values()).stream().filter(v -> v.getValue().equals(value)).findFirst().get();
	}

}
