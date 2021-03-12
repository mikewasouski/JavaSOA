package com.acme.binary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.transport.http.auth.HttpAuthHeader;
import org.apache.cxf.ws.security.tokenstore.SecurityToken;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.wss4j.common.token.BinarySecurity;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.stereotype.Component;

import com.acme.ClientPasswordCallback;

@Component
public class wsBinary extends RouteBuilder{

		@Override
		public void configure() throws Exception {
			
			//intercept().to("log:hello");
			
			List<Interceptor<? extends org.apache.cxf.message.Message>> lst = new ArrayList<Interceptor<? extends org.apache.cxf.message.Message>>();
			//lst.add(new CxfInterceptor());
			
			Map<String, Object> serverInProps = new HashMap<>();
			serverInProps.put(WSHandlerConstants.SIG_ALGO, "http://www.w3.org/2000/09/xmldsig#rsa-sha1" ); 
			serverInProps.put(WSHandlerConstants.SIG_C14N_ALGO, "http://www.w3.org/2001/10/xml-exc-c14n#" ); 
			serverInProps.put(WSHandlerConstants.ENC_DIGEST_ALGO, "http://www.w3.org/2000/09/xmldsig#sha1" );
			serverInProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.SIGNATURE);
		    serverInProps.put(WSHandlerConstants.SIG_PROP_FILE, "server_key.properties");
		    serverInProps.put(WSHandlerConstants.SIG_KEY_ID, "DirectReference");
		    
		    
			WSS4JInInterceptor inInterceptor = new WSS4JInInterceptor(serverInProps);
			lst.add(inInterceptor);
			
			CxfEndpoint endpointFrom = new CxfEndpoint();
			endpointFrom.setInInterceptors(lst);
		    //endpoint.setDataFormat(DataFormat.PAYLOAD);
		    endpointFrom.setWsdlURL("src/main/resources/employee.wsdl");
		    endpointFrom.setAddress("http://localhost:9000/binary");
		    endpointFrom.setServiceClass("org.mikew.employee_ws.EmployeeWs");
		     
		    
		    getContext().getRegistry().bind("cxfBinary", endpointFrom);
		    from("cxf:bean:cxfBinary").process( new Processor(){
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
		

		private SecurityToken createSecurityToken(BinarySecurity binarySecurityToken) {
	  	  SecurityToken token = new SecurityToken(binarySecurityToken.getID());
	  	  token.setToken(binarySecurityToken.getElement());
	  	  token.setSecret(binarySecurityToken.getToken());
	  	  token.setTokenType(binarySecurityToken.getValueType());
	  	  return token;
	  	}

		
	}