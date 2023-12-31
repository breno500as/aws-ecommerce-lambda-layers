package com.br.aws.ecommerce.layers.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShippingDTO {

	private String type;

	private String carrier;

	 

	@JsonCreator
	public ShippingDTO(@JsonProperty("type") String type, @JsonProperty("carrier") String carrier) {
		this.type = type;
		this.carrier = carrier;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

}
