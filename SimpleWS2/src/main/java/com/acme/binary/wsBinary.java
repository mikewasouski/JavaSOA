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
			
			Map<String, Object> inProperties = new HashMap<>();
			//inProperties.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
			inProperties.put(WSHandlerConstants.ACTION, WSHandlerConstants.SIGNATURE);
			inProperties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
			inProperties.put(WSHandlerConstants.PW_CALLBACK_CLASS, ClientPasswordCallback.class.getName());
			
			//inProperties.put("action", "UsernameTokenNoPassword");

			//inProperties.put("passwordType", "PasswordText");
			//inProperties.put("user", "usr");
			
			WSS4JInInterceptor inInterceptor = new WSS4JInInterceptor(inProperties);
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