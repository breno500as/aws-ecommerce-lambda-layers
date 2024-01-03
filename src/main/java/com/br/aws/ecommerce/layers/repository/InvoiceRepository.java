package com.br.aws.ecommerce.layers.repository;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.br.aws.ecommerce.layers.base.BaseLambdaFunction;
import com.br.aws.ecommerce.layers.entity.InvoiceEntity;

public class InvoiceRepository extends BaseLambdaFunction<InvoiceEntity> { 
	
	private Logger logger = Logger.getLogger(InvoiceRepository.class.getName());

	private DynamoDB dynamoDB;

	private String tableInvoice;
	

	public InvoiceRepository(AmazonDynamoDB amazonDynamoDB, String tableName) {
		this.dynamoDB = new DynamoDB(amazonDynamoDB);
		this.tableInvoice = tableName;
	}
	
	
   public InvoiceEntity save(InvoiceEntity invoice) {
		
		try {

			 
			final Table table = this.dynamoDB.getTable(this.tableInvoice);

			final Item item = new Item().withPrimaryKey("pk", invoice.getPk(), 
			        "sk", invoice.getSk())
					.withLong("ttl", invoice.getTtl())
					.withLong("createdAt", invoice.getCreatedAt())
					.withLong("totalValue", invoice.getTotalValue())
					.withInt("quantity", invoice.getQuantity())
					.withString("productId", invoice.getProductId())
					.withString("transactionId", invoice.getTransactionId());
					 
			table.putItem(item);
			
			return invoice;  
			
			 

		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot save invoice: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}
		
	}


}
