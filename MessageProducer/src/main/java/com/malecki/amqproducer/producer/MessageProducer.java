/**
 * 
 */
package com.malecki.amqproducer.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

/**
 * @author Krystian Małecki
 *
 */
public class MessageProducer {
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	public void send(String destination, String message) {
	    jmsTemplate.convertAndSend(destination, message);
	}
}
