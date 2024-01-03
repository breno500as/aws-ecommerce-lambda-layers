package com.br.aws.ecommerce.layers.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.br.aws.ecommerce.layers.base.BaseLambdaFunction;
import com.br.aws.ecommerce.layers.entity.OrderEntity;

public class OrderRepository extends BaseLambdaFunction<OrderEntity> {

	private Logger logger = Logger.getLogger(OrderRepository.class.getName());

	private DynamoDB dynamoDB;

	private String tableOrder;
	
	private static final String FIELDS_PROJECTION_WITHOUT_PRODUCTS = "pk, sk, createdAt, shipping, billing";

	public OrderRepository(AmazonDynamoDB amazonDynamoDB, String tableName) {
		this.dynamoDB = new DynamoDB(amazonDynamoDB);
		this.tableOrder = tableName;
	}

	public OrderEntity save(OrderEntity order) {

		try {

			 
			final Table table = this.dynamoDB.getTable(this.tableOrder);
			
			order.setCreatedAt(Instant.now().toEpochMilli());


			final Item item = new Item().withPrimaryKey("pk", order.getPk(), 
			        "sk", UUID.randomUUID().toString())
					.withLong("createdAt", order.getCreatedAt())
					.withJSON("shipping", getMapper().writeValueAsString(order.getShipping()))
					.withJSON("billing", getMapper().writeValueAsString(order.getBilling()))
					.withJSON("products", getMapper().writeValueAsString(order.getProducts()));
				 
			table.putItem(item);
			
			return order;  
			
			 

		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot save product: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}
	}

 
	public void deleteByEmailAndOrderId(String email, String orderId) {

		try {

			final Table table = this.dynamoDB.getTable(this.tableOrder);
			table.deleteItem("pk", email, "sk", orderId);

		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot delete order: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}

	}

	 
	public List<OrderEntity> findAll(boolean getProducts) {

		try {

			final Table table = this.dynamoDB.getTable(this.tableOrder);

			ScanSpec scanSpec ;

			if (getProducts) {
				scanSpec  = new ScanSpec();
			} else {
				scanSpec  = new ScanSpec()
				    // Faz o filtro por outros campos da tabela, sempre utilizar scan para filtrar
				    // por atributos que não são a PK, para filtrar pela PK utilizar getItem  de Table ou QuerySpec
					//	.withFilterExpression("#createdAt = :createdAt")
					//	.withNameMap(new NameMap().with("#createdAt", "createdAt"))
					//	.withValueMap(new ValueMap().withLong(":createdAt", 1704144250391l))
						.withProjectionExpression(FIELDS_PROJECTION_WITHOUT_PRODUCTS);
			}

		 

            return super.formatJsonResponse(table.scan(scanSpec));

		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot find all orders: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}

	}

 
	public List<OrderEntity> findByEmail(String email) {

		try {

			final Table table = this.dynamoDB.getTable(this.tableOrder);
			
			 final QuerySpec spec = new QuerySpec() 
					   .withKeyConditionExpression("pk = :email") 
					   .withProjectionExpression(FIELDS_PROJECTION_WITHOUT_PRODUCTS)
					   .withValueMap(new ValueMap() 
					   .withString(":email", email));

		    return super.formatJsonResponse(table.query(spec));
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot order by email: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}
	}
	
	public OrderEntity findByEmailAndOrderId(String email, String orderId) {

		try {

			final Table table = this.dynamoDB.getTable(this.tableOrder);

			final Item item = table.getItem("pk", email, "sk", orderId);

			if (item == null) {
				throw new RuntimeException("Product not found!");
			}

			return super.getMapper().readValue(item.toJSON(), OrderEntity.class);
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot order by orderId: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}
	}
	
	
}
