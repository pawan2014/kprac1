package pk.com.kfexample.kfkalearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class MessageConsumer {
	private Logger log = LoggerFactory.getLogger(MessageConsumer.class);

	@KafkaListener(topics = "${myapp.kafka.topic}", 
			groupId = "xyz",
			containerFactory = "customKafkaListenerContainerFactory"
			)
	public void consume(String message) {
		log.info("MESSAGE recieved -> " + message);
		// messageRepo.addMessage(message);
	}
	
	
}
