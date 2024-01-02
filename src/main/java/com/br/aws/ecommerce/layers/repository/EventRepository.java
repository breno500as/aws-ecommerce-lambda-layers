package com.br.aws.ecommerce.layers.repository;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.br.aws.ecommerce.layers.base.BaseLambdaFunction;
import com.br.aws.ecommerce.layers.entity.EventEntity;
import com.br.aws.ecommerce.layers.entity.ProductEntity;
import com.br.aws.ecommerce.layers.model.OrderEnvelopeDTO;
import com.br.aws.ecommerce.layers.model.OrderEventDTO;
import com.br.aws.ecommerce.layers.model.ProductEventDTO;

public class EventRepository extends BaseLambdaFunction<EventEntity> {

	private Logger logger = Logger.getLogger(EventRepository.class.getName());

	private DynamoDB dynamoDB;

	private String tableEvent = "event";

	public EventRepository(AmazonDynamoDB amazonDynamoDB) {
		this.dynamoDB = new DynamoDB(amazonDynamoDB);
	}
	
	
	public void saveProductEvent(ProductEventDTO productEvent) {

		try {

			final Table table = this.dynamoDB.getTable(this.tableEvent);
			
			final long timestamp = Instant.now().toEpochMilli();
			final long ttl = Instant.now().plus(Duration.ofMinutes(10)).getEpochSecond();	 

			final Item item = new Item()
					.withPrimaryKey("pk", "#product_" + productEvent.getProductCode(), 
							        "sk", "#" + productEvent.getProductEventType().getValue() + "#" + timestamp)
					.withLong("createdAt", timestamp)
					.withString("requestId", productEvent.getRequestId())
					.withString("eventType", productEvent.getProductEventType().getValue())
					.withJSON("info", getMapper().writeValueAsString(new ProductEventDTO(productEvent.getProductId(), productEvent.getProductPrice())))
					.withLong("ttl", ttl);
			
			if (productEvent.getEmail() != null) {
				item.withString("email", productEvent.getEmail());
			}

			table.putItem(item);


		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot save event: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}
	}
	
	
	public void saveOrderEvent(OrderEventDTO orderEvent, OrderEnvelopeDTO orderEnvelope) {

		try {

			final Table table = this.dynamoDB.getTable(this.tableEvent);
			
			final long timestamp = Instant.now().toEpochMilli();
			final long ttl = Instant.now().plus(Duration.ofMinutes(10)).getEpochSecond();
			
			
			  List<ProductEntity> emptyProducts = null;
			
			if (orderEvent.getProducts() != null) {
				emptyProducts = orderEvent.getProducts().stream().map(p -> new ProductEntity(p.getId(),p.getCode())).collect(Collectors.toList());
			}
			 
			final Item item = new Item()
					.withPrimaryKey("pk", "#order_" + UUID.randomUUID().toString(), 
							        "sk", orderEnvelope.getEventType().getValue() + "#" + timestamp)
					.withString("email", orderEvent.getEmail())
					.withLong("createdAt", timestamp)
					.withString("messageId", orderEvent.getMessageId())
					.withString("requestId", orderEvent.getRequestId())
					.withString("eventType", orderEnvelope.getEventType().getValue())
					.withJSON("info", getMapper().writeValueAsString(new OrderEventDTO(orderEvent.getOrderId(), orderEvent.getMessageId(), emptyProducts)))
					.withLong("ttl", ttl);
			
			table.putItem(item);


		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot save event: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}
	}


	public List<EventEntity> findByEmail(String email) {
		try {

			final Table table = this.dynamoDB.getTable(this.tableEvent);
			
			final Index index = table.getIndex("emailIndex");
			
			 final QuerySpec spec = new QuerySpec()
					   .withKeyConditionExpression("email = :email") 
					   .withValueMap(new ValueMap() 
					   .withString(":email", email));

			return super.formatJsonResponse(index.query(spec)); 
			
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot get events by email: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}
	}


	public List<EventEntity> findByEmailAndEventType(String email, String eventType) {
		try {

			final Table table = this.dynamoDB.getTable(this.tableEvent);
			
			final Index index = table.getIndex("emailIndex");
			
			 final QuerySpec spec = new QuerySpec() 
					   .withKeyConditionExpression("email = :email AND begins_with(sk, :prefix)") 
					   .withValueMap(new ValueMap() 
					   .withString(":email", email)
					   .withString(":prefix", eventType));

			 return super.formatJsonResponse(index.query(spec)); 
			 
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot get events by email: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}
	}

}
