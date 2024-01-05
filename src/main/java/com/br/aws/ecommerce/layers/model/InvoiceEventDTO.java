package com.br.aws.ecommerce.layers.model;

public class InvoiceEventDTO {

	private String pk;

	private String email;

	private String transactionId;

	private String productId;
	
	private String status;
	
	

	public InvoiceEventDTO(String pk, String email, String transactionId, String productId, String status) {
		super();
		this.pk = pk;
		this.email = email;
		this.transactionId = transactionId;
		this.productId = productId;
		this.status = status;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

}
