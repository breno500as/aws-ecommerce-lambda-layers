package com.br.aws.ecommerce.layers.repository;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableKeysAndAttributes;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.BatchGetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.br.aws.ecommerce.layers.base.BaseLambdaFunction;
import com.br.aws.ecommerce.layers.entity.ProductEntity;
import com.fasterxml.jackson.core.type.TypeReference;

public class ProductRepository extends BaseLambdaFunction {

	private Logger logger = Logger.getLogger(ProductRepository.class.getName());

	private DynamoDB dynamoDB;

	private String tableProducts = "product";

	public ProductRepository(AmazonDynamoDB amazonDynamoDB) {
		this.dynamoDB = new DynamoDB(amazonDynamoDB);
	}

	
 
	public ProductEntity update(ProductEntity product, String uuid) {

		try {

			final Table table = this.dynamoDB.getTable(this.tableProducts);

			final UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("id", uuid)
					.withUpdateExpression("set #name = :val1, #code = :val2, #price = :val3, #model = :val4")
					.withConditionExpression("attribute_exists(id)")
					.withNameMap(new NameMap().with("#name", "name").with("#code", "code").with("#price", "price")
							.with("#model", "model"))
					.withValueMap(
							new ValueMap().withString(":val1", product.getName())
							              .withString(":val2", product.getCode())
									      .withNumber(":val3", product.getPrice())
									      .withString(":val4", product.getModel()))
					.withReturnValues(ReturnValue.ALL_NEW);

			UpdateItemOutcome updateItemOutcome = table.updateItem(updateItemSpec);

			return super.getMapper().readValue(updateItemOutcome.getItem().toJSON(), ProductEntity.class);
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot update product: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}

	}

 
	public ProductEntity save(ProductEntity product) {

		try {

			final Table table = this.dynamoDB.getTable(this.tableProducts);

			final String id = UUID.randomUUID().toString();

			final Item item = new Item().withPrimaryKey("id", id).withString("name", product.getName())
					.withString("code", product.getCode()).withNumber("price", product.getPrice())
					.withString("model", product.getModel());
				 

			table.putItem(item);

			product.setId(id);

			return product;

		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot save product: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}
	}

 
	public void delete(String uuid) {

		try {

			final Table table = this.dynamoDB.getTable(this.tableProducts);
			table.deleteItem("id", uuid);

		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot delete product: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}

	}

	 
	public List<ProductEntity> findAll() {

		try {

			final Table table = this.dynamoDB.getTable(this.tableProducts);

			final ItemCollection<ScanOutcome> itemCollection = table.scan();

			final StringBuilder sb = new StringBuilder();

			sb.append("[");

			for (Item item : itemCollection) {
				sb.append(item.toJSON()).append(",");
			}

			sb.append("]");

			return super.getMapper().readValue(sb.toString().replace(",]", "]"), new TypeReference<List<ProductEntity>>() {
			});

		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot find all products: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}

	}

 
	public ProductEntity findById(String id) {

		try {

			final Table table = this.dynamoDB.getTable(this.tableProducts);

			final Item item = table.getItem("id", id);

			if (item == null) {
				throw new RuntimeException("Product not found!");
			}

			return super.getMapper().readValue(item.toJSON(), ProductEntity.class);
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot product by id: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}
	}
	
	
	 
 
	public List<ProductEntity> getByIds(List<String> ids) {

		try {
			
			
			final TableKeysAndAttributes tableKeysAndAttributes = new TableKeysAndAttributes(this.tableProducts);
			
			tableKeysAndAttributes.addHashOnlyPrimaryKeys("id", ids.toArray(new String[ids.size()]));
			
			final BatchGetItemSpec spec = new BatchGetItemSpec() .withTableKeyAndAttributes(tableKeysAndAttributes);
			
			final List<Item> itens = this.dynamoDB.batchGetItem(spec).getTableItems().get(this.tableProducts);
			
			final StringBuilder sb = new StringBuilder();

			sb.append("["); 

			for (Item item : itens) {
				sb.append(item.toJSON()).append(",");
			}
 
			sb.append("]"); 
		   
		    
			this.logger.log(Level.INFO, String.format("Lista concatenada: %s", sb.toString()));

		    return super.getMapper().readValue(sb.toString().replace(",]", "]"), new TypeReference<List<ProductEntity>>() {
			});
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Cannot get products by ids: %s", e.getMessage()), e);
			throw new RuntimeException(e);
		}
	}

}
