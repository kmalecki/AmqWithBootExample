package com.malecki.amqconsumer.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

/**
 * The {@link MSG_COUNTER} is needed ONLY for simple test.
 * 
 * @author Krystian Małecki
 *
 */
public class MsgConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MsgConsumer.class);
	public static volatile int MSG_COUNTER = 0;

	@JmsListener(destination = "${activemq.queue}")
	public void receive(String message) {
		LOGGER.info("Received message='{}'", message);
		MsgConsumer.MSG_COUNTER++;
	}
}
