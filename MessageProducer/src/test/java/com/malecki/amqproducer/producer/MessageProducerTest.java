package com.malecki.amqproducer.producer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.PostConstruct;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageProducerTest {


	private static ApplicationContext applicationContext;

	@Value("${activemq.broker-url}")
	private String brokerUrl;

	@Value("${activemq.queue}")
	private String queueName;

	@Autowired
	private MessageProducer msgProducer;

	private ConnectionFactory connectionFactory;
	private MessageConsumer consumer;

	@PostConstruct
	public void initAmqBroker() throws JMSException {
		connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		Connection createConnection = connectionFactory.createConnection();
		createConnection.start();
		Session session = createConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue createQueue = session.createQueue(queueName);
		consumer = session.createConsumer(createQueue);
	}

	@Autowired
	void setContext(ApplicationContext applicationContext) {
		MessageProducerTest.applicationContext = applicationContext;
	}

	@AfterClass
	public static void afterClass() {
		((ConfigurableApplicationContext) applicationContext).close();
	}

	@Test
	public void messageProducerTest() throws Exception {
		String messageToSend = "Hello JMS!";

		msgProducer.send(queueName, messageToSend);
		ActiveMQTextMessage receive = (ActiveMQTextMessage) consumer.receive(100);
		String body = receive.getText();

		assertNotNull(body);
		assertEquals("Recieved invalid message.", messageToSend, body);
	}
}
