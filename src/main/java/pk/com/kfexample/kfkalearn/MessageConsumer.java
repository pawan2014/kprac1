package pk.com.kfexample.kfkalearn;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import pk.com.kfexample.kfkalearn.service.MonitorService;

/**
 * You can receive messages by configuring a MessageListenerContainer and providing a message listener or by using the @KafkaListener annotation.
 * currently eight supported interfaces for message listeners.
 * @author pk
 *
 */
@Component
public class MessageConsumer {
	private Logger log = LoggerFactory.getLogger(MessageConsumer.class);
	
	@Autowired
	private  MonitorService  mserv;
	
	/*
	 * Simple listener
	@KafkaListener(topics = "${myapp.kafka.topic}", groupId = "xyz", containerFactory = "customKafkaListenerContainerFactory")
	public void consume(String message) {
		log.info("MESSAGE recieved -> " + message);

	}
	*/

	/**
	 * Get the consumerrecord and have a customized listenerContainerFactory
	 * @param record
	 */
	@KafkaListener(
			id="general-reader-id",
			topics = {"${myapp.kafka.topic}" }, 
			groupId = "group2", 
			containerFactory = "customKafkaListenerContainerFactory")
	public void consume(ConsumerRecord<Integer, String> record) {
		
		String[] sp = record.value().split(";");
		mserv.update(sp[0], sp[1], record.offset(), record.offset());
		log.info("received = " + record.value() + " with key " + record.key() + " offset=" + record.offset()
				+ " headers=" + record.headers());
		
		

	}
	
}
