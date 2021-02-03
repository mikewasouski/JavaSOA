package com.exdemo.builder;

import cxf.com.codenotfound.types.helloworld.Person;

public class HelloReqest {

	 public Person setPerson(String Name, String LastName) {
         Person persona = new Person();
         persona.setFirstName(Name);
         persona.setLastName(LastName);

         return persona;
     }
	 
	 public Person setPerson(String Name) {
         Person persona = new Person();
         persona.setFirstName(Name);
         persona.setLastName("hard codded");

         return persona;
     }
	 
	 public HelloReqest() {
		 
	 }
}
