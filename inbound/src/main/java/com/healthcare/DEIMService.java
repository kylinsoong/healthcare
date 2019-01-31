package com.healthcare;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.customer.app.Person;
import com.customer.app.response.ESBResponse;

@RestController
public class DEIMService {
	
	@Produce(uri = "direct:pingRoute")  
	ProducerTemplate pingTemplate;
	
	@Produce(uri = "direct:matchRoute")  
	ProducerTemplate matchTemplate;
	
	@RequestMapping(path = "deim/api/ping", method = RequestMethod.GET)
	public String ping() {
		
		Map<String, Object> headers = new HashMap<String, Object>();
	    headers.put("METHOD", "ping");
	    
	    String camelResponse = pingTemplate.requestBodyAndHeaders(pingTemplate.getDefaultEndpoint(), "PING FROM DEIM Service", headers, String.class);
	    
	    return camelResponse;
	}
	
	@RequestMapping(path = "deim/api/match", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE})
	public ESBResponse addPerson(Person person) {
		
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
