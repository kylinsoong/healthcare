package com.nextgate.service;

import javax.annotation.PostConstruct;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.nextgate.model.ExecuteMatchUpdate;

@Component
public class ExecuteMatchUpdateRepository {
	
	private static final Set<ExecuteMatchUpdate> updates = new HashSet<>();

	@PostConstruct
	public void initData() {
	}

	public String add(ExecuteMatchUpdate update) {
		updates.add(update);
		
		System.out.println("Processed " + update.getGivenName() + " " + update.getFamilyName());
		
		return "Current set size: " + updates.size();
	}
}
