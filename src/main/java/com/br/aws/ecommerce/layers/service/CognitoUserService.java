package com.br.aws.ecommerce.layers.service;

import java.util.Map;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.br.aws.ecommerce.layers.model.CognitoUserDTO;

public class CognitoUserService {

	private AWSCognitoIdentityProvider cognitoClient;

 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CognitoUserDTO getAuthenticatedUser(final APIGatewayProxyRequestEvent input,
			AWSCognitoIdentityProvider cognitoClient) {

		this.cognitoClient = cognitoClient;

		final Map<String, String> claims = (Map) input.getRequestContext().getAuthorizer().get("claims");

		final String poolId = claims.get("iss").replaceAll("https://", "").split("/")[1];

		final String username = claims.get("username");

		final AdminGetUserResult user = this.cognitoClient
				.adminGetUser(new AdminGetUserRequest().withUserPoolId(poolId).withUsername(username));
		
		
        final CognitoUserDTO cognitoUser = new CognitoUserDTO();
       
       cognitoUser.setUsername(user.getUsername());
       cognitoUser.setEnabled(user.getEnabled());
       
	    user.getUserAttributes().stream().forEach(attribute -> {
	     
	    	if (attribute.getName().equals("email")) {
	    		cognitoUser.setEmail(attribute.getValue());
	    	}
	    	
	    	if (attribute.getName().equals("sub")) {
	    		cognitoUser.setSub(attribute.getValue());
	    	}
	    	
	    });
	    
	    return cognitoUser;
	}

}
