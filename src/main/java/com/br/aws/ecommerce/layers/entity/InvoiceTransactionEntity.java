package com.br.aws.ecommerce.layers.entity;

import com.br.aws.ecommerce.layers.model.InvoiceTranscationStatus;

public class InvoiceTransactionEntity {

	private String pk;

	private String sk;

	private String requestId;

	private long ttl;

	private long timestamp;

	private long expiresIn;

	private String connectionId;

	private InvoiceTranscationStatus invoiceTranscationStatus;

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getSk() {
		return sk;
	}

	public void setSk(String sk) {
		this.sk = sk;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public long getTtl() {
		return ttl;
	}

	public void setTtl(long ttl) {
		this.ttl = ttl;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}

	public InvoiceTranscationStatus getInvoiceTranscationStatus() {
		return invoiceTranscationStatus;
	}

	public void setInvoiceTranscationStatus(InvoiceTranscationStatus invoiceTranscationStatus) {
		this.invoiceTranscationStatus = invoiceTranscationStatus;
	}

}
