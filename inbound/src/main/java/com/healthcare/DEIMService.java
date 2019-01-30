package com.healthcare;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DEIMService {
	
	@Produce(uri = "direct:pingRoute")  
	ProducerTemplate pingTemplate;
	
	@RequestMapping(path = "deim/api/ping", method = RequestMethod.GET)
	public String ping() {
		
		Map<String, Object> headers = new HashMap<String, Object>();
	    headers.put("METHOD", "ping");
	    
	    String camelResponse = pingTemplate.requestBodyAndHeaders(pingTemplate.getDefaultEndpoint(), "PING FROM DEIM Service", headers, String.class);
	    
	    return camelResponse;
	}

}
