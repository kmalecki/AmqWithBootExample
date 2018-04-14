package com.malecki.amqconsumer.consumer;

import static org.junit.Assert.assertTrue;

import javax.annotation.PostConstruct;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
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
public class MsgConsumerTest {

	private static ApplicationContext applicationContext;

	@Value("${activemq.broker-url}")
	private String brokerUrl;

	@Value("${activemq.queue}")
	private String queueName;

	@Autowired
	private MsgConsumer msgConsumer;

	private ConnectionFactory connectionFactory;
	private MessageProducer producer;
	private Session session;
	private Queue testQueue;

	@PostConstruct
	public void initAmqBroker() throws JMSException {
		connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		Connection createConnection = connectionFactory.createConnection();
		createConnection.start();
		session = createConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		testQueue = session.createQueue(queueName);
		producer = session.createProducer(testQueue);
	}

	@Autowired
	void setContext(ApplicationContext applicationContext) {
		MsgConsumerTest.applicationContext = applicationContext;
	}

	@AfterClass
	public static void afterClass() {
		((ConfigurableApplicationContext) applicationContext).close();
	}

	@Test
	public void messageProducerTest() throws Exception {
		int initCount = MsgConsumer.MSG_COUNTER;
		String messageToSend = "Consumer Hello JMS test.";
		TextMessage txtMsg = session.createTextMessage(messageToSend);
		producer.send(txtMsg);
		Thread.sleep(100); // make a pause to be sure that environment did not influence the performance of
							// consumer.
		assertTrue(MsgConsumer.MSG_COUNTER - initCount == 1);
	}
}
