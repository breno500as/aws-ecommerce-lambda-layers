package com.br.aws.ecommerce.layers.entity;

import java.math.BigDecimal;

public class ProductEntity {

	private String id;

	private String name;

	private String code;

	private BigDecimal price;

	private String model;

	public ProductEntity() {

	}

	public ProductEntity(String id) {
		this.id = id;
	}	
	
	public ProductEntity(String id, String code) {
		this.id = id;
		this.code = code;
	}	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

}
