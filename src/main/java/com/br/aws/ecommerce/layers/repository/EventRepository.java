package com.br.aws.ecommerce.layers.repository;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.br.aws.ecommerce.layers.base.BaseLambdaFunction;
import com.br.aws.ecommerce.layers.model.ProductEventDTO;

public class EventRepository extends BaseLambdaFunction {

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

}
