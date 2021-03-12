package com.acme.signature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class SignatureWsSec extends RouteBuilder {
	@Value("${server.ticketagent.keystore-alias}")
	private String keystoreAlias;
	
	@Override
	public void configure() throws Exception {
		
		List<Interceptor<? extends org.apache.cxf.message.Message>> lst = new ArrayList<Interceptor<? extends org.apache.cxf.message.Message>>();
		
		Map<String, Object> serverInProps = new HashMap<>();
	    serverInProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.TIMESTAMP + " " + WSHandlerConstants.SIGNATURE);
	    serverInProps.put(WSHandlerConstants.SIG_PROP_FILE, "server_trust.properties");

		WSS4JInInterceptor inInterceptor = new WSS4JInInterceptor(serverInProps);
		lst.add(inInterceptor);
		
		Map<String, Object> serverOutProps = new HashMap<>();
	    serverOutProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.TIMESTAMP + " " + WSHandlerConstants.SIGNATURE);
	    serverOutProps.put(WSHandlerConstants.USER, keystoreAlias);
	    serverOutProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, SignCallback.class.getName());
	    serverOutProps.put(WSHandlerConstants.SIG_PROP_FILE, "server_key.properties");
	    
		WSS4JInInterceptor outInterceptor = new WSS4JInInterceptor(serverOutProps);
		lst.add(outInterceptor);
		
		CxfEndpoint endpointFrom = new CxfEndpoint();
		endpointFrom.setInInterceptors(lst);
	    //endpoint.setDataFormat(DataFormat.PAYLOAD);
	    endpointFrom.setWsdlURL("src/main/resources/employee.wsdl");
	    endpointFrom.setAddress("http://localhost:9000/sign");
	    endpointFrom.setServiceClass("org.mikew.employee_ws.EmployeeWs");
	    getContext().getRegistry().bind("cxfSignature", endpointFrom);
	    
	    from("cxf:bean:cxfSignature").process( new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception{
				Message message = exchange.getIn();
				//String body = message.getBody().toString();
				MessageContentsList result = (MessageContentsList)message.getBody();
				System.out.println("\nReceived output text: " + result.get(0).getClass().getName());
				//Person gr = (Person)result.get(0);
				//System.out.println("\nReceived output text: " +gr.getFirstName() );
				
				}
			}
		)
	    .to("mock:test");
	   
	}
	

	/*private SecurityToken createSecurityToken(BinarySecurity binarySecurityToken) {
  	  SecurityToken token = new SecurityToken(binarySecurityToken.getID());
  	  token.setToken(binarySecurityToken.getElement());
  	  token.setSecret(binarySecurityToken.getToken());
  	  token.setTokenType(binarySecurityToken.getValueType());
  	  return token;
  	}
*/
	
}

