package com.br.aws.ecommerce.layers.service;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApi;
import com.amazonaws.services.apigatewaymanagementapi.model.DeleteConnectionRequest;
import com.amazonaws.services.apigatewaymanagementapi.model.DeleteConnectionResult;
import com.amazonaws.services.apigatewaymanagementapi.model.GetConnectionRequest;
import com.amazonaws.services.apigatewaymanagementapi.model.GetConnectionResult;
import com.amazonaws.services.apigatewaymanagementapi.model.PostToConnectionRequest;
import com.amazonaws.services.apigatewaymanagementapi.model.PostToConnectionResult;

public class InvoiceWSService {
	
	private Logger logger = Logger.getLogger(InvoiceWSService.class.getName());
	
	private AmazonApiGatewayManagementApi client;
	
	public InvoiceWSService(AmazonApiGatewayManagementApi client) {
		this.client = client;
	}
	
	public boolean sendInvoiceStatus(String connectionId, String transactionId, String status) {
		final String data =  "{\"trascationId\": \"" + transactionId + "\""  
		+ ",\"status\": \"" + status + "\"}";
		return this.sendData(connectionId, data);
	}
	
	public boolean disconnectClient(String connectionId) {
		
       try {
			
			final GetConnectionRequest getConnectionRequest = new GetConnectionRequest()
					.withConnectionId(connectionId);
			 
			final GetConnectionResult result1 = this.client.getConnection(getConnectionRequest);
			this.logger.log(Level.INFO, String.format("GetConnectionResult httpStatusCode: %s ", result1.getSdkHttpMetadata().getHttpStatusCode()));
			
			final DeleteConnectionRequest deleteConnection = new DeleteConnectionRequest()
					.withConnectionId(connectionId);
			
			final DeleteConnectionResult deleteConnectionResult = this.client.deleteConnection(deleteConnection);
			this.logger.log(Level.INFO, String.format("DeleteConnectionResult httpStatusCode(): %s ", deleteConnectionResult.getSdkHttpMetadata().getHttpStatusCode()));
			
			return true;
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Error sendData: %s ", e.getMessage()), e);
			return false;
		}
		
	}
	
	public boolean sendData(String connectionId, String data) {
		
		try {
			
			final GetConnectionRequest getConnectionRequest = new GetConnectionRequest()
					.withConnectionId(connectionId);
	 
			final GetConnectionResult result1 = this.client.getConnection(getConnectionRequest);
			this.logger.log(Level.INFO, String.format("GetConnectionResult httpStatusCode: %s ", result1.getSdkHttpMetadata().getHttpStatusCode()));
			
			final PostToConnectionRequest post = new PostToConnectionRequest()
					.withData(ByteBuffer.wrap(data.getBytes()))
					.withConnectionId(connectionId);
 
			this.logger.log(Level.INFO, "Post data: {0}", data);
			final PostToConnectionResult p = this.client.postToConnection(post);
			this.logger.log(Level.INFO, String.format("PostToConnectionResult httpStatusCode(): %s ", p.getSdkHttpMetadata().getHttpStatusCode()));
			
			return true;
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, String.format("Error sendData: %s ", e.getMessage()), e);
			return false;
		}
	 
		
	}
	
	 

}
