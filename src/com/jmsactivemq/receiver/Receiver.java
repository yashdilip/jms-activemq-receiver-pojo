package com.jmsactivemq.receiver;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.jmsactivemq.receiver.model.User;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class Receiver implements MessageListener {
	
	private ConnectionFactory factory = null;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageConsumer consumer = null;

	public Receiver(){}
	
	public void receiveMessage(){
		try {
			factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("MYQUEUE");
			consumer = session.createConsumer(destination);
			consumer.setMessageListener(this);
			System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
			
			//Message message = consumer.receive();
			
			/*if(message instanceof TextMessage){
				TextMessage msg = (TextMessage) message;
				System.out.println("Message received is "+ msg.getText());
			}*/
			/*if(message instanceof ObjectMessage){
				ObjectMessage obj = (ObjectMessage) message;
				User usr = (User) obj.getObject();
				
				
				System.out.println(usr.toString());
			}*/
		} catch (JMSException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Receiver receiver = new Receiver();
		receiver.receiveMessage();

	}

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		
		try {
			/*if(message instanceof TextMessage){
				TextMessage msg = (TextMessage) message;
				System.out.println("Message received is "+ msg.getText());
			}*/
			
			/*ObjectMessage objectMessage = (ObjectMessage) message;
			User person = (User)objectMessage.getObject();
			if (null != person) {
				System.out.println(person.toString());
			}*/
			
			XStream xstream = new XStream(new StaxDriver());
			xstream.alias("user", com.jmsactivemq.receiver.model.User.class);
			TextMessage tmsg = (TextMessage)message;
			User user = (User)xstream.fromXML(tmsg.getText());

			System.out.println("Received Object: "+user.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
