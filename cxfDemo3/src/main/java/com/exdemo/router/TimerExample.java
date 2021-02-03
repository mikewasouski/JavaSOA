package com.exdemo.router;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class TimerExample extends RouteBuilder{
	
	@Override
	public void configure() throws Exception {

		//from("timer://simpleTimer?period=40000")
		from("direct:timer")
		.setBody(simple("Hello from timer at ${header.firedTime}"))
		//.log(LoggingLevel.INFO, "Log From Timer")
		.to("stream:out");
	}
}
