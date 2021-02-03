package com.exdemo.router;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class StreamExample extends RouteBuilder{
	
	@Override
	public void configure() throws Exception {
		//System.out.println("/*/*/*/*/*/*/*/*/*/");
		fromF("stream:in?promptMessage=Say something: StreamExample")
		//.transform(simple("${body.toUpperCase()}"))
		//.log(LoggingLevel.INFO, "Log from stream example.")
		.to("stream:out");
		
	}

}
