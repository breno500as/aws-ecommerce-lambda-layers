package com.br.aws.ecommerce.layers.repository;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.br.aws.ecommerce.layers.base.BaseLambdaFunction;
import com.br.aws.ecommerce.layers.entity.InvoiceTransactionEntity;
import com.br.aws.ecommerce.layers.model.InvoiceTranscationStatus;

public class InvoiceTranscationRepository  extends BaseLambdaFunction<InvoiceTransactionEntity> { 
	
	
	private Logger logger = Logger.getLogger(InvoiceTranscationRepository.class.getName());

	private DynamoDB dynamoDB;

	private String tableInvoice;
	
	public static final String PK_TRANSACTION = "#transaction";
	

	public InvoiceTranscationRepository(AmazonDynamoDB amazonDynamoDB, String tableName) {
		this.dynamoDB = new DynamoDB(amazonDynamoDB);
		this.tableInvoice = tableName;
	}
	
	public InvoiceTransactionEntity save(InvoiceTransactionEntity invoiceTransaction) {
		
		try {

			 
			final Table table = this.dynamoDB.getTable(this.tableInvoice);

			final Item item = new Item().withPrimaryKey("pk", invoiceTransaction.getPk(), 
			        "sk", invoiceTransaction.getSk())
					.withLong("ttl", invoiceTransaction.getTtl())
					.withLong("expiresIn", invoiceTransaction.getExpiresIn())
					.withLong("timestamp", invoiceTransaction.getTimestamp())
					.withString("invoiceTranscationStatus", invoiceTransaction.getInvoiceTranscationStatus().getValue())
					.withString("connectionId", invoiceTransaction.getConnectionId())
					.withString("requestId", invoiceTransaction.getRequestId());
					 
			table.putItem(item);
			
			return invoiceTransaction;  
			
			 

		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot save invoice: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}
		
	}
	
	public InvoiceTransactionEntity getByKey(String key) {
		try {

			final Table table = this.dynamoDB.getTable(this.tableInvoice);

			final Item item = table.getItem("pk", PK_TRANSACTION, "sk", key);

			if (item == null) {
				throw new RuntimeException("Invoice not found!");
			}

			return super.getMapper().readValue(item.toJSON(), InvoiceTransactionEntity.class);
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot invoice by key: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}
	}
	
	public InvoiceTransactionEntity update(String key, InvoiceTranscationStatus status) {
		try {

			final Table table = this.dynamoDB.getTable(this.tableInvoice);

			final UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("pk", PK_TRANSACTION, "sk", key)
					.withUpdateExpression("set #invoiceTranscationStatus = :val1")
					.withConditionExpression("attribute_exists(pk)")
					.withNameMap(new NameMap().with("#invoiceTranscationStatus", "invoiceTranscationStatus"))
					.withValueMap(new ValueMap().withString(":val1", status.getValue()))          
					.withReturnValues(ReturnValue.ALL_NEW);

			UpdateItemOutcome updateItemOutcome = table.updateItem(updateItemSpec);

			return super.getMapper().readValue(updateItemOutcome.getItem().toJSON(), InvoiceTransactionEntity.class);
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot update product: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}
	}


}
