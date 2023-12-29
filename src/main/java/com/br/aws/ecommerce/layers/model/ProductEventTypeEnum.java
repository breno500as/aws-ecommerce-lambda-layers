package com.br.aws.ecommerce.layers.model;

public enum ProductEventTypeEnum {
	
	CREATED("PRODUCT_CREATED"),
	UPDATED("PRODUCT_UPDATED"),
	DELETED("PRODUCT_DELETED");
	
	private String value;
	
	private ProductEventTypeEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
			

}
