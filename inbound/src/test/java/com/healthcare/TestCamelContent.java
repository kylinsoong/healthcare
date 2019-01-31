package com.healthcare;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestCamelContent extends CamelSpringTestSupport {
	
	@Produce(uri = "direct:pingRoute") 
	private ProducerTemplate pingTemplate;
	
	@Produce(uri = "direct:xml-test-input") 
	private ProducerTemplate startEndpoint;
	
	@EndpointInject(uri = "mock:xml-test-output") 
	private MockEndpoint resultEndpoint;


	@Override
	protected AbstractApplicationContext createApplicationContext() {

		return new ClassPathXmlApplicationContext("spring/camel-context.xml");
	}
	
	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {

			DataFormat jaxb = new JaxbDataFormat("com.customer.app");
			@Override
			public void configure() throws Exception {
				from("direct:xml-test-input")
				.log("${body}")
				.unmarshal(jaxb)
				.to("mock:xml-test-output");
			}};
	}

	@Test
	public void testPing() {
		
		Map<String, Object> headers = new HashMap<String, Object>();
	    headers.put("METHOD", "ping");
	    
	    String camelResponse = pingTemplate.requestBodyAndHeaders(pingTemplate.getDefaultEndpoint(), "PING FROM TEST", headers, String.class);
	    
	    assertEquals("PONG FROM CAMEL CONTEXT", camelResponse);
		
	}
	
	@Test
	public void testTransform() throws Exception {
		
		resultEndpoint.expectedMessageCount(1);
		startEndpoint.sendBody(readFile("src/test/data/SimplePatient.xml"));
		resultEndpoint.assertIsSatisfied();
	}

	private Object readFile(String filePath) throws Exception {
		String content;
        FileInputStream fis = new FileInputStream(filePath);
        try {
            content = createCamelContext().getTypeConverter().convertTo(String.class, fis);
        } finally {
            fis.close();
        }
        return content;
	}

}
