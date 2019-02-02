package com.nextgate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.nextgate.model.GetExecuteMatchUpdateRequest;
import com.nextgate.model.GetExecuteMatchUpdateResponse;;

@Endpoint
public class ExecuteMatchUpdateEndpoint {
	private static final String NAMESPACE_URI = "http://nextgate.com/nextgate-service";

	private ExecuteMatchUpdateRepository countryRepository;

	@Autowired
	public ExecuteMatchUpdateEndpoint(ExecuteMatchUpdateRepository countryRepository) {
		this.countryRepository = countryRepository;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetExecuteMatchUpdateRequest")
	@ResponsePayload
	public GetExecuteMatchUpdateResponse getCountry(@RequestPayload GetExecuteMatchUpdateRequest request) {
		GetExecuteMatchUpdateResponse response = new GetExecuteMatchUpdateResponse();
		response.setName(countryRepository.add(request.getExecuteMatchUpdate()));

		return response;
	}
}
