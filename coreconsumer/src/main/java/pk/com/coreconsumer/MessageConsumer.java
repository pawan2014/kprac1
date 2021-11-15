package pk.com.coreconsumer;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
	public void consume(ConsumerRecord<String, String> record) {
		//String[] sp = record.value().split(";");
		log.info("received = " + record.value() + " with key " + record.key() + " offset=" + record.offset()
				+ " headers=" + record.headers());

	}
	*/

	@KafkaListener(groupId = "CC-GROUP", id = "batch-listener", topics = "${myapp.kafka.topic}" , containerFactory = "customKafkaListenerContainerFactory")
	public void receive(List<String> data, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
			@Header(KafkaHeaders.OFFSET) List<Long> offsets) {

		log.info("start of batch receive="+data.size());
		for (int i = 0; i < data.size(); i++) {
			log.info("received message='{}' with partition-offset='{}'", data.get(i),
					partitions.get(i) + "-" + offsets.get(i));
			updateRecordSet(partitions.get(i),offsets.get(i));
			try {
				// induce some delays
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void updateRecordSet(Integer integer, Long aLong){

		RestTemplate template = new RestTemplate();

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://localhost:9091/monitor/currentOffset/update").
		queryParam("group", "FORNOW-HARDCODED").
		queryParam("topic", "MY-TEST-TOPIC_1").
		queryParam("partition", integer.toString()).
		queryParam("offset", aLong.toString());

		HttpHeaders headers = new HttpHeaders();
		HttpEntity requestEntity = new HttpEntity<>(headers);
		URI sp = builder.buildAndExpand().toUri();
		System.out.println(sp);
		ResponseEntity<String> response1 = template.exchange(sp, HttpMethod.GET, requestEntity,String.class);
	}


}
