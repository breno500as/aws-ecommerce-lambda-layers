package com.br.aws.ecommerce.layers.model;

import java.util.List;

import com.br.aws.ecommerce.layers.entity.ProductEntity;

public class OrderEventDTO {

	private String email;

	private String orderId;

	private ShippingDTO shipping;

	private BillingDTO billing;

	private List<ProductEntity> products;

	private String requestId;

	private String messageId;
	
	public OrderEventDTO() {
		
	}
	
	public OrderEventDTO(String orderId, String messageId, List<ProductEntity> products) {
		this.orderId = orderId;
		this.messageId = messageId;
		this.products = products;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public ShippingDTO getShipping() {
		return shipping;
	}

	public void setShipping(ShippingDTO shipping) {
		this.shipping = shipping;
	}

	public BillingDTO getBilling() {
		return billing;
	}

	public void setBilling(BillingDTO billing) {
		this.billing = billing;
	}

	public List<ProductEntity> getProducts() {
		return products;
	}

	public void setProducts(List<ProductEntity> products) {
		this.products = products;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

}
