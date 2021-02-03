package com.exdemo.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import com.exdemo.builder.HelloReqest;

import cxf.com.codenotfound.types.helloworld.Person;

public class GreetingProccessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		String payload = exchange.getIn().getBody(String.class);
        // do something with the payload and/or exchange here
		HelloReqest req = new HelloReqest();
		Person a = new Person();
		a.setFirstName("mike");
		a.setLastName("wass");
		req.setPerson("Miguel", "Angel");
        exchange.getIn().setBody(req);
	}

}
