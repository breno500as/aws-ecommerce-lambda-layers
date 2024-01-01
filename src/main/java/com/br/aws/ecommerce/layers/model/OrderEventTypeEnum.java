package com.br.aws.ecommerce.layers.model;

public enum OrderEventTypeEnum {

	CREATED("ORDER_CREATED"), DELETED("ORDER_DELETED");

	private String value;

	private OrderEventTypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
