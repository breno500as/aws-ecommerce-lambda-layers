package com.br.aws.ecommerce.layers.base;

import java.util.List;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseLambdaFunction<T> {

	final ObjectMapper mapper = new ObjectMapper();

	public ObjectMapper getMapper() {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(Include.NON_NULL);
		return this.mapper;
	}

	public List<T> formatJsonResponse(ItemCollection<?> items) throws JsonMappingException, JsonProcessingException {

		if (items == null) {
			throw new RuntimeException("Item not found!");
		}

		final StringBuilder sb = new StringBuilder();

		sb.append("[");

		for (Item item : items) {
			sb.append(item.toJSON()).append(",");
		}

		sb.append("]");

		return getMapper().readValue(sb.toString().replace(",]", "]"), new TypeReference<List<T>>() {
		});

	}

	public List<T> formatJsonResponse(List<Item> items) throws JsonMappingException, JsonProcessingException {

		if (items == null) {
			throw new RuntimeException("Item not found!");
		}

		final StringBuilder sb = new StringBuilder();

		sb.append("[");

		for (Item item : items) {
			sb.append(item.toJSON()).append(",");
		}

		sb.append("]");

		return getMapper().readValue(sb.toString().replace(",]", "]"), new TypeReference<List<T>>() {
		});

	}

}
