package com.br.aws.ecommerce.layers.model;

public class ErrorMessageDTO {

	private String error;

	public ErrorMessageDTO(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
