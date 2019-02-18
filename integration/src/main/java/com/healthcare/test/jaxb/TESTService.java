package com.healthcare.test.jaxb;

import java.util.UUID;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.customer.app.Code;
import com.customer.app.Person;
import com.customer.app.PersonName;
import com.customer.app.response.ESBResponse;
import com.healthcare.test.jaxb.model.Customer;

@RestController
@RequestMapping
public class TESTService {
	
	Logger log = LoggerFactory.getLogger(TESTService.class);

	
	@RequestMapping(path = "/deim/api/ping", method = RequestMethod.GET)
	public String ping() {
	       
	    return "PONG";
	}
	
	@RequestMapping(path = "/deim/api/person", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE})
	public Person getPerson() {
	       
		Person person = new Person();
    	person.setAge(30);
    	PersonName pname = new PersonName();
    	pname.setGiven("First");
    	pname.setFamily("Last");
    	person.setLegalname(pname);
    	person.setFathername("Dad");
    	person.setMothername("Mom");
    	Code code = new Code();
    	code.setCode("Male");
    	person.setGender(code);
    	
	    return person;
	}
	
	@RequestMapping(path = "/deim/api/match", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE})
	public ESBResponse addPerson(@RequestBody Person person) {
		
		log.info("Receive Request: " + person);
		log.info("    age: " + person.getAge());
//		log.info("        givenrname: " + person.getLegalname().getGiven());
//		log.info("        familyname: " + person.getLegalname().getFamily());
		log.info("    fathername: " + person.getFathername());
		log.info("    mothername: " + person.getMothername());
//		log.info("        gender: " + person.getGender().getCode());
		log.info("");
		
		
	    ESBResponse esbResponse = new ESBResponse();
	    esbResponse.setBusinessKey(UUID.randomUUID().toString());
	    esbResponse.setPublished(true);
	    
	    String comment = "NONE";
	      
	    esbResponse.setComment(comment);
	    
	    return esbResponse;
	}
	
	 @GetMapping("/customer")
	 public Customer getCustomer(){
		 return new Customer("Peter", "Smith", 30);
	 }
	 
	 @PostMapping("/customer")
	 public String postCustomer(@RequestBody Customer customer){
		 System.out.println(customer);
		 return "Done";
	 }

}
