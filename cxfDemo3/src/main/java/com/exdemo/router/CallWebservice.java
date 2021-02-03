package com.exdemo.router;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.springframework.stereotype.Component;

import com.exdemo.builder.GreetingRequestBuilder;
import com.exdemo.builder.HelloReqest;

import cxf.com.codenotfound.types.helloworld.Person;

@Component
public class CallWebservice extends RouteBuilder{
		
		@Override
		public void configure() throws Exception {
			
			from("timer://simpleTimer?period=15000").
			to("direct:in_ws2");
			
			from("direct:in_ws")
			.log(LoggingLevel.INFO, "->>>Start")
			.setBody(constant("12345"))
            .bean(GreetingRequestBuilder.class)
			.to("stream:out")
			//.process("GreetingProcessor")
			.setHeader(CxfConstants.OPERATION_NAME, constant("sayHello"))

		    .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("http://codenotfound.com/services/helloworld"))
		    .to("cxf://http://localhost:9090/codenotfound/ws/helloworld"
		            + "?serviceClass=cxf.com.codenotfound.services.helloworld.HelloWorldPortType"
		            + "&wsdlURL=/helloWorld.wsdl")
			.log(LoggingLevel.INFO, "1 After call WS through BEAN: ${body[0].greeting}")
			.to("mock:output");
			
			
			
			from("direct:in_ws2")
			.log(LoggingLevel.INFO, "->>>Start")
			.process( new Processor(){
				@Override
				public void process(Exchange exchange) throws Exception{
						Person persona = new Person();
						persona.setFirstName("John");
						persona.setLastName("Smith");
						exchange.getIn().setBody(persona);
					}
				}
			)
			.bean(GreetingRequestBuilder.class)
			.setHeader(CxfConstants.OPERATION_NAME, constant("sayHello"))

		    .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("http://codenotfound.com/services/helloworld"))
		    .to("cxf://http://localhost:9090/codenotfound/ws/helloworld"
		            + "?serviceClass=cxf.com.codenotfound.services.helloworld.HelloWorldPortType"
		            + "&wsdlURL=/helloWorld.wsdl")
			.log(LoggingLevel.INFO, "1 After call WS through BEAN: ${body[0].greeting}")
			.to("mock:output");
			
		}
		
		
		

}
