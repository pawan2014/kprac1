package pk.com.kfexample.kfkalearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.extern.slf4j.Slf4j;

@Component

public class MessageProducer {
	private Logger log = LoggerFactory.getLogger(MessageProducer.class);

	@Autowired
	private KafkaTemplate<String, String> customKafkaTemplate;

	@Value("${myapp.kafka.topic}")
	private String topic;

	public void sendMessage(String message) {
		log.info("MESSAGE SENT FROM PRODUCER END -> " + message);
		//customKafkaTemplate.send(topic, message);

		ListenableFuture<SendResult<String, String>> future = this.customKafkaTemplate.send(topic, message);

		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
			@Override
			public void onSuccess(SendResult<String, String> result) {
				log.info("Sent message: " + message + " with offset: " + result.getRecordMetadata().offset());
			}

			@Override
			public void onFailure(Throwable ex) {
				log.error("Unable to send message : " + message, ex);
			}
		});
	}
}
