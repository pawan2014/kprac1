package pk.com.kfexample.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component

public class MessageProducer {
	private Logger log = LoggerFactory.getLogger(MessageProducer.class);

	@Autowired
	private KafkaTemplate<String, String> customKafkaTemplate;

	@Value("${myapp.kafka.topic}")
	private String topic;

	public void sendMessage(String key,String message) {

		ListenableFuture<SendResult<String, String>> future = this.customKafkaTemplate.send(topic,key, message);
		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
			@Override
			public void onSuccess(SendResult<String, String> result) {
				log.info("Sent topic#{}, P#{} offset#{} ",result.getRecordMetadata().topic(),
						result.getRecordMetadata().partition(),
						result.getRecordMetadata().offset());
			}

			@Override
			public void onFailure(Throwable ex) {
				log.error("Unable to send message : " + message, ex);
			}
		});
	}
}
