package com.br.aws.ecommerce.layers.base;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseLambdaFunction {

	final ObjectMapper mapper = new ObjectMapper();

	public ObjectMapper getMapper() {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(Include.NON_NULL);
		return this.mapper;
	}

}
