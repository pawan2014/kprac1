package pk.com.kfexample.kfkalearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component

public class MessageProducer {
    private Logger log = LoggerFactory.getLogger(MessageProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${myapp.kafka.topic}")
    private String topic;

    public void sendMessage(String message) {
        //log.info("MESSAGE SENT FROM PRODUCER END -> " + message);
        kafkaTemplate.send(topic, message);
    }

    @Scheduled(fixedDelay = 1000)
    public void sendMessage() {
        sendMessage(UUID.randomUUID().toString());
    }
}
