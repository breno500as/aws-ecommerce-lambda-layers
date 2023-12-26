package com.br.aws.ecommerce.layers.repository;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.br.aws.ecommerce.layers.base.BaseLambdaFunction;
import com.br.aws.ecommerce.layers.model.ProductDTO;
import com.fasterxml.jackson.core.type.TypeReference;

public class ProductRepository extends BaseLambdaFunction {

	private Logger logger = Logger.getLogger(ProductRepository.class.getName());

	private DynamoDB dynamoDB;

	private String tableProducts = "product";
	
	 

	public ProductRepository(AmazonDynamoDB amazonDynamoDB) {
		this.dynamoDB = new DynamoDB(amazonDynamoDB);
	}
	
	
	public ProductDTO update(ProductDTO product, String uuid) {
		
	      try {
	    	  
	    	  final Table table = this.dynamoDB.getTable(this.tableProducts);
 
	          final UpdateItemSpec updateItemSpec = new UpdateItemSpec() 
	            .withPrimaryKey("id", uuid)
	            .withUpdateExpression("set name = :name, code=:code, price=:price, model=:model")
	              .withValueMap(new ValueMap() 
	              .withString(":name", product.getName())
	              .withString(":code", product.getCode())
	              .withString(":price", product.getPrice())
                  .withString(":model", product.getModel())) 
	            .withConditionExpression("id = :id")
	              .withValueMap(new ValueMap() 
	    	      .withString(":id", uuid))
	            .withReturnValues(ReturnValue.UPDATED_NEW);  
	          
	          UpdateItemOutcome updateItemOutcome =  table.updateItem(updateItemSpec);  
	         
	          return super.getMapper().readValue(updateItemOutcome.getItem().toJSONPretty(), ProductDTO.class);     
	      } catch (Exception e) { 
	    	 this.logger.log(Level.SEVERE, "Cannot update product: %s" , e.getMessage());
	    	 throw new RuntimeException(e); 
	      }         
		
	}
	
	public ProductDTO save(ProductDTO product) {
		
		try {
	
		  final Table table = this.dynamoDB.getTable(this.tableProducts);
		
		  final Item item = new Item() 
		            .withPrimaryKey("id", UUID.randomUUID().toString())
		            .withString("name", product.getName())
		            .withString("code", product.getCode())
		            .withString("price", product.getPrice())
		            .withString("model", product.getModel());
		
		  final PutItemOutcome putItemOutcome  = table.putItem(item);  
		        
		  return super.getMapper().readValue(putItemOutcome.getItem().toJSONPretty(), ProductDTO.class);
		
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, "Cannot save product: %s" , e.getMessage());
			throw new RuntimeException(e);
		}  
	}
	
	public void delete(String uuid) {

		try {

			final Table table = this.dynamoDB.getTable(this.tableProducts);
			table.deleteItem("id", uuid);

		} catch (Exception e) {
			this.logger.log(Level.SEVERE, "Cannot delete product: %s", e.getMessage());
			throw new RuntimeException(e);
		}

	}

	public List<ProductDTO> findAll() {

		try {

			final Table table = this.dynamoDB.getTable(this.tableProducts);

			final ItemCollection<ScanOutcome> itemCollection = table.scan();

			String itensStr = "";

			for (Item item : itemCollection) {
				itensStr += item.toJSONPretty() + ",";
			}

			return super.getMapper().readValue(itensStr, new TypeReference<List<ProductDTO>>() {});

		} catch (Exception e) {
			this.logger.log(Level.SEVERE, "Cannot find all products: %s" , e.getMessage());
			throw new RuntimeException(e);
		}  

	}

	public ProductDTO findById(String id) {

		try {

			final Table table = this.dynamoDB.getTable(this.tableProducts);

			final Item item = table.getItem("id", id);

			return super.getMapper().readValue(item.toJSONPretty(), ProductDTO.class);
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, "Cannot find product by id: %s" , e.getMessage());
			throw new RuntimeException(e);
		} 
	}

}
