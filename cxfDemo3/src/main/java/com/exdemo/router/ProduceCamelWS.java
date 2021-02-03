package com.exdemo.router;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ProduceCamelWS extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		 //from("cxf:bean:proxyEndpoint") // bean config
	     //.log(LoggingLevel.INFO, "${body}")
	     //.setBody(constant("12345"))
	     //.to("cxf://http://localhost:9090/codenotfound/ws/helloworld");
		 //.to("mock:service");
		 
		from("cxf://http://localhost:9000/router?" +                        
                "serviceClass=cxf.com.codenotfound.services.helloworld.HelloWorldPortType" +
                //"&serviceName={http://axis.webservice.vnitt.com/}NewWebService" +
                //"&portName={http://axis.webservice.vnitt.com/}NewWebServicePort" +
                "&wsdlURL=src/main/resources/helloWorld.wsdl").process( new Processor(){
    				@Override
    				public void process(Exchange exchange) throws Exception{
    					System.out.println("unwind and alter message body here");
    					}
    				}
    			).to("cxf://http://localhost:9090/codenotfound/ws/helloworld"
    		            + "?serviceClass=cxf.com.codenotfound.services.helloworld.HelloWorldPortType"
    		            + "&wsdlURL=/helloWorld.wsdl");
		 
		 
	}
}