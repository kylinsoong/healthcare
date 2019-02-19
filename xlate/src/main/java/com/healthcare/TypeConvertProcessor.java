package com.healthcare;

import java.io.ObjectOutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import com.customer.app.Person;
import com.nextgate.model.ExecuteMatchUpdate;
import com.nextgate.model.Gender;
import com.nextgate.model.GetExecuteMatchUpdateRequest;

public class TypeConvertProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		Object in = exchange.getIn().getBody();
		Person p = (Person) in ;
	
		ObjectOutputStream oos = null;
		try {
			ExecuteMatchUpdate match = new ExecuteMatchUpdate();
			match.setAge(p.getAge());
			match.setFamilyName(p.getLegalname().getFamily());
			match.setGivenName(p.getLegalname().getGiven());
			match.setFatherName(p.getFathername());
			match.setMotherName(p.getMothername());
			if(p.getGender().getCode().equals("Male")) {
				match.setGender(Gender.M);
			} else {
				match.setGender(Gender.F);
			}
			
			GetExecuteMatchUpdateRequest request = new GetExecuteMatchUpdateRequest();
			request.setExecuteMatchUpdate(match);
			
			Message msg = exchange.getIn();
			
			// write object inputstream
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			oos = new ObjectOutputStream(baos);
//			oos.writeObject(match);
//			oos.flush();		
//			msg.setBody(new ByteArrayInputStream(baos.toByteArray()));
			
			msg.setBody(request);
			
			exchange.setOut(msg);
		} catch (Exception e) {
			throw new TypeConvertException(e.getMessage());
		} finally {
			if(null != oos) {
				oos.close();
			}
		}
	}

}
