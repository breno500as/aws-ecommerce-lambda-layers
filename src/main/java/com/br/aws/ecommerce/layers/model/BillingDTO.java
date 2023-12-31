package com.br.aws.ecommerce.layers.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BillingDTO {

	private BigDecimal totalPrice;

	private String payment;

	public BillingDTO() {

	}

	@JsonCreator
	public BillingDTO(@JsonProperty("totalPrice") BigDecimal totalPrice, @JsonProperty("payment") String payment) {
		super();
		this.totalPrice = totalPrice;
		this.payment = payment;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

}
