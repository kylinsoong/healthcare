/*
 * Copyright 2016 Red Hat, Inc.
 * <p>
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */
package com.healthcare;

import java.io.IOException;
import java.io.StringWriter;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.QueueConnection;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.apache.qpid.jms.JmsQueue;

import com.customer.app.Code;
import com.customer.app.Person;
import com.customer.app.PersonName;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;


public class AMQPSender {
	
	

    /**
     * A main method to start this application.
     * @throws JMSException 
     * @throws JAXBException 
     * @throws IOException 
     */
    public static void main(String[] args) throws JMSException, JAXBException, IOException {

    	JmsConnectionFactory factory = new JmsConnectionFactory("admin", "admin", "amqp://localhost:5672");
   
        
        QueueConnection conn = factory.createQueueConnection();
        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(new JmsQueue("q.empi.deim.in"));
        
        TextMessage message = session.createTextMessage(create());
        
        System.out.println("Sent message: \n" + message.getText());
        
        producer.send(message);
        
        conn.close();
    }
    
    private static String create() throws JAXBException, IOException {
    	JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
    	
    	Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
    	jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    	jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.app.customer.com/PatientDemographics.xsd");
    	jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapper() {
    		
    		private static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";
            private static final String PERSON_URI = "http://www.app.customer.com";
    		
            @Override
           public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
            	
            	if(XSI_URI.equals(namespaceUri)) {
            		return "xsi";
            	} else if (PERSON_URI.equals(namespaceUri)) {
            		return "p";
            	}
               return suggestion;
           }
            
            @Override
            public String[] getPreDeclaredNamespaceUris() {
                return new String[] { XSI_URI, PERSON_URI};
            }
       });
    	
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
    	
    	StringWriter writer = new StringWriter();
    	jaxbMarshaller.marshal(person, writer);
    	
    	String message = writer.toString();
    	
    	writer.close();
    	
    	return message;
    }


}