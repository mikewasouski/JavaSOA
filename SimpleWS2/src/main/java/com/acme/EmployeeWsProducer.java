package com.acme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfEndpoint;
//import org.apache.camel.component.jetty.JettyHttpComponent;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.cxf.binding.soap.interceptor.MustUnderstandInterceptor;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.transport.http.auth.HttpAuthHeader;
import org.apache.cxf.ws.security.tokenstore.SecurityToken;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.wss4j.common.token.BinarySecurity;
import org.mikew.employee_ws.Employee;
import org.springframework.stereotype.Component;

import com.acme.CxfInterceptor;

//import org.apache.cxf.ws.security.trust.STSClient;


@Component
public class EmployeeWsProducer extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		
		//intercept().to("log:hello");
		
		List<Interceptor<? extends org.apache.cxf.message.Message>> lst = new ArrayList<Interceptor<? extends org.apache.cxf.message.Message>>();
		//lst.add(new CxfInterceptor());
		
		Map<String, Object> inProperties = new HashMap<>();
		inProperties.put("action", "UsernameTokenNoPassword");

		//inProperties.put("passwordType", "PasswordText");
		inProperties.put("user", "usr");
		WSS4JInInterceptor inInterceptor = new WSS4JInInterceptor(inProperties);
		lst.add(inInterceptor);
		
		CxfEndpoint endpointFrom = new CxfEndpoint();
		endpointFrom.setInInterceptors(lst);
	    //endpoint.setDataFormat(DataFormat.PAYLOAD);
	    endpointFrom.setWsdlURL("src/main/resources/employee.wsdl");
	    endpointFrom.setAddress("http://localhost:9000/router");
	    endpointFrom.setServiceClass("org.mikew.employee_ws.EmployeeWs");
	    
	    
	    //
	    Map<String, Object> properties = new HashMap<String, Object>(); 

	    AuthorizationPolicy authPolicy = new AuthorizationPolicy(); 
	    authPolicy.setAuthorizationType(HttpAuthHeader.AUTH_TYPE_BASIC); 
	    authPolicy.setUserName("usr"); 
	    authPolicy.setPassword("pwd"); 
	    authPolicy.setAuthorization("true");
	   
	    properties.put("org.apache.cxf.configuration.security.AuthorizationPolicy", authPolicy);

	    //CxfEndpoint myCxfEp = (CxfEndpoint)getContext().getEndpoint("cxf://");
	    endpointFrom.setProperties(properties);
	    ////2
	    
	    
	    
	    getContext().getRegistry().bind("cxfendJDSL", endpointFrom);
	    
	    from("cxf:bean:cxfendJDSL").process( new Processor(){
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
	
	/*
	private void configureJetty() {
		KeyStoreParameters ksp = new KeyStoreParameters();
		ksp.setResource("E://temp//certificates//oracle//clientkeystore");
		ksp.setPassword("asdfgh");
		KeyManagersParameters kmp = new KeyManagersParameters();
		kmp.setKeyStore(ksp); kmp.setKeyPassword("asdfgh");
		SSLContextParameters scp = new SSLContextParameters();
		scp.setKeyManagers(kmp);
		JettyHttpComponent jettyComponent = getContext().getComponent("jetty", JettyHttpComponent.class);
		jettyComponent.setSslContextParameters(scp);
		}
	
	private void configureHttp4() {
		KeyStoreParameters ksp = new KeyStoreParameters();
		ksp.setResource("\Projects\example\exampleCa.jks");
		ksp.setPassword("password");
		TrustManagersParameters tmp = new TrustManagersParameters();
		tmp.setKeyStore(ksp);
		SSLContextParameters scp = new SSLContextParameters();
		scp.setTrustManagers(tmp);
		HttpComponent httpComponent = getContext().getComponent("https4", HttpComponent.class);
		httpComponent.setSslContextParameters(scp);
		}/*/

	private SecurityToken createSecurityToken(BinarySecurity binarySecurityToken) {
  	  SecurityToken token = new SecurityToken(binarySecurityToken.getID());
  	  token.setToken(binarySecurityToken.getElement());
  	  token.setSecret(binarySecurityToken.getToken());
  	  token.setTokenType(binarySecurityToken.getValueType());
  	  return token;
  	}

	
}

