package com.healthcare;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class TypeConvertProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		Object in = exchange.getIn().getBody();
		
		System.out.println(in.getClass());
		System.out.println(in);
		
		Message msg = exchange.getIn();
		msg.setBody(new String("@@@@@@@@@@@@"));
		exchange.setOut(msg);
	}

}
