package com.malecki.amqproducer.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.malecki.amqproducer.producer.MessageProducer;

/**
 * Not splitting into interface and implementation for clarity and focus on the messaging.
 * 
 * @author Krystian Małecki
 *
 */
@RestController
public class RestApi {

	/**
	 * Default queue. See application.yml.
	 */
	@Value("${activemq.queue}")
	private String defaultDestination;
	
	@Autowired
	private MessageProducer producer;

	/**
	 * 
	 * @param message
	 *            Simple String. Example: Hello there, young wolf.
	 * @param destination
	 *            The queue name stored in RequestHeader. Not required. If not
	 *            present the {@link RestApi#defaultDestination} is used.
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void postMessage(@RequestBody(required = true) String message,
			@RequestHeader(required = false) String destination) {
		producer.send(destination == null ? defaultDestination : destination, message);
	}
}
