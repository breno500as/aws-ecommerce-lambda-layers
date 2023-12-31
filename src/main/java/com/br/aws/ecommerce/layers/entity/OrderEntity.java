package com.br.aws.ecommerce.layers.entity;

import java.util.List;

import com.br.aws.ecommerce.layers.model.BillingDTO;
import com.br.aws.ecommerce.layers.model.ShippingDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderEntity {

	@JsonProperty("email")
	private String pk;

	@JsonProperty("orderId")
	private String sk;

	private ShippingDTO shipping;

	private long createdAt;

	private List<ProductEntity> products;

	private BillingDTO billing;

	private List<String> idsProducts;

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

	public ShippingDTO getShipping() {
		return shipping;
	}

	public void setShipping(ShippingDTO shipping) {
		this.shipping = shipping;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public List<ProductEntity> getProducts() {
		return products;
	}

	public void setProducts(List<ProductEntity> products) {
		this.products = products;
	}

	public BillingDTO getBilling() {
		return billing;
	}

	public void setBilling(BillingDTO billing) {
		this.billing = billing;
	}

	public List<String> getIdsProducts() {
		return idsProducts;
	}

	public void setIdsProducts(List<String> idsProducts) {
		this.idsProducts = idsProducts;
	}

}
