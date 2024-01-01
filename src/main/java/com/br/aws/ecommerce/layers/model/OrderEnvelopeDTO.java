package com.br.aws.ecommerce.layers.model;

public class OrderEnvelopeDTO {

	private OrderEventTypeEnum eventType;
	private String data;

	public OrderEventTypeEnum getEventType() {
		return eventType;
	}

	public void setEventType(OrderEventTypeEnum eventType) {
		this.eventType = eventType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
