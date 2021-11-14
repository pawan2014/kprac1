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
 * You can receive messages by configuring a MessageListenerContainer and
 * providing a message listener or by using the @KafkaListener annotation.
 * currently eight supported interfaces for message listeners.
 * 
 * @author pk
 *
 */
@Component
public class MessageConsumer {
	private Logger log = LoggerFactory.getLogger(MessageConsumer.class);

	@Autowired
	private MonitorService mserv;

	/*
	 * Simple listener
	 * 
	 * @KafkaListener(topics = "${myapp.kafka.topic}", groupId = "xyz",
	 * containerFactory = "customKafkaListenerContainerFactory") public void
	 * consume(String message) { log.info("MESSAGE recieved -> " + message);
	 * 
	 * }
	 */

	/**
	 * Get the consumerrecord and have a customized listenerContainerFactory
	 * 
	 * @param record
	 */
	/*
	@KafkaListener(id = "general-reader-id", topics = {
			"${myapp.kafka.topic}" }, groupId = "group2", containerFactory = "customKafkaListenerContainerFactory")
	public void consume(ConsumerRecord<Integer, String> record) {

		String[] sp = record.value().split(";");
		mserv.update(sp[0], sp[1], record.offset(), record.offset());
		log.info("received = " + record.value() + " with key " + record.key() + " offset=" + record.offset()
				+ " headers=" + record.headers());

	}
	*/

	@KafkaListener(id = "batch-listener", topics = "${myapp.kafka.topic}" , containerFactory = "customKafkaListenerContainerFactory")
	public void receive(List<String> data, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
			@Header(KafkaHeaders.OFFSET) List<Long> offsets) {

		log.info("start of batch receive="+data.size());
		for (int i = 0; i < data.size(); i++) {
			log.info("received message='{}' with partition-offset='{}'", data.get(i),
					partitions.get(i) + "-" + offsets.get(i));
			
			String[] sp = data.get(i).split(";");
			mserv.update(sp[0], sp[1], offsets.get(i), offsets.get(i));
			
			try {
				// induce some delays
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		log.info("end of batch receive");
	}

}
