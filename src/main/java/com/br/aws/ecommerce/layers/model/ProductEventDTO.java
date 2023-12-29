package com.br.aws.ecommerce.layers.model;

import java.math.BigDecimal;

public class ProductEventDTO {

	private String productId;

	private String productCode;

	private BigDecimal productPrice;

	private String email;

	private String requestId;

	private ProductEventTypeEnum productEventType;
	
	public ProductEventDTO() {
		
	}
	
	public ProductEventDTO(String productId, BigDecimal price) {
		this.productId = productId;
		this.productPrice = price;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ProductEventTypeEnum getProductEventType() {
		return productEventType;
	}

	public void setProductEventType(ProductEventTypeEnum productEventType) {
		this.productEventType = productEventType;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Override
	public String toString() {
		return "ProductEventDTO [productId=" + productId + ", productCode=" + productCode + ", productPrice="
				+ productPrice + ", email=" + email + ", productEventType=" + productEventType + "]";
	}

}
