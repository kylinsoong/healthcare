package com.healthcare;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.customer.app.Person;
import com.customer.app.response.ESBResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping
public class DEIMService {
	
	Logger log = LoggerFactory.getLogger(DEIMService.class);
	
	@Produce(uri = "direct:pingRoute")  
	ProducerTemplate pingTemplate;
	
	@Produce(uri = "direct:matchRoute")  
	ProducerTemplate matchTemplate;
	
	@RequestMapping(path = "/deim/api/ping", method = RequestMethod.GET)
	@ApiOperation(value = "ping", notes = "Ping Camel Content")
	public String ping() {
		
		Map<String, Object> headers = new HashMap<String, Object>();
	    headers.put("METHOD", "ping");
	    
	    String camelResponse = pingTemplate.requestBodyAndHeaders(pingTemplate.getDefaultEndpoint(), "PING FROM DEIM Service", headers, String.class);
	    
	    return camelResponse;
	}
	
	@RequestMapping(path = "/deim/api/match", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE})
	@ApiOperation(
            value = "Add a patient",
            notes = "Add a patient with xml format")
	public ESBResponse addPerson(Person person) {
		
		log.info("Receive Request: " + person);
		log.info("    age: " + person.getAge());
//		log.info("        givenrname: " + person.getLegalname().getGiven());
//		log.info("        familyname: " + person.getLegalname().getFamily());
		log.info("    fathername: " + person.getFathername());
		log.info("    mothername: " + person.getMothername());
//		log.info("        gender: " + person.getGender().getCode());
		log.info("");
		
		Map<String, Object> headers = new HashMap<String, Object>();
	    headers.put("METHOD", "match");
	    
	    String camelResponse = matchTemplate.requestBodyAndHeaders(matchTemplate.getDefaultEndpoint(), person, headers, String.class);
	    
	    ESBResponse esbResponse = new ESBResponse();
	    esbResponse.setBusinessKey(UUID.randomUUID().toString());
	    esbResponse.setPublished(true);
	    
	    String comment = "NONE";
	      
	    if (camelResponse.equals("0")) {
	    	comment = "NO MATCH";
	    } else if (camelResponse.equals("1")) {
	        comment = "MATCH";
	    } else if (camelResponse.equals("2")) {
	        comment = "DONE";
	    } else {
	        comment = "ERROR";
	    }
	    esbResponse.setComment(comment);
	    
	    return esbResponse;
	}

}
