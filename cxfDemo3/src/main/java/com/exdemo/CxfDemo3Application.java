package com.exdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import cxf.com.codenotfound.endpoint.HelloWorldImplService;
import cxf.com.codenotfound.types.helloworld.Person;

@SpringBootApplication
@ImportResource("classpath:appContext.xml")
public class CxfDemo3Application {

	public static void main(String[] args) {
		SpringApplication.run(CxfDemo3Application.class, args);
		//callWs();
	}

	public static void callWs() {
		Person persona = new Person();
		persona.setFirstName("Mike");
		persona.setLastName("Rdz");
		
		HelloWorldImplService greet = new HelloWorldImplService();
		String result = greet.getHelloWorldImplPort().sayHello(persona).getGreeting();
		System.out.println(result);
			
    }
}
