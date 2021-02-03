package com.exdemo.builder;

import cxf.com.codenotfound.endpoint.HelloWorldImplService;
import cxf.com.codenotfound.types.helloworld.Greeting;
import cxf.com.codenotfound.types.helloworld.Person;

public class GreetingRequestBuilder {
	
	public Person getGreeting(String name) {
		Person persona = new Person();
		persona.setFirstName(name);
		persona.setLastName("Rdz");
		return persona;
	}
	
	public Person getGreeting(Person persona) {
		persona.setFirstName(persona.getFirstName());
		persona.setLastName(persona.getLastName());
		return persona;
	}

}
