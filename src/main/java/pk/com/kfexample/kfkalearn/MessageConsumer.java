package pk.com.kfexample.kfkalearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class MessageConsumer {
    private Logger log = LoggerFactory.getLogger(MessageConsumer.class);

    @KafkaListener(id = "DYNA-LST", topics = "${myapp.kafka.topic}", groupId = "ex9-0", autoStartup = "false")
    public void consume(String message) {
        log.info("MESSAGE recieved -> " + message);
        // messageRepo.addMessage(message);
    }

    @KafkaListener(id = "NONDYNA-LST", topics = "${myapp.kafka.topic}", groupId = "ex9-1")
    public void consume2(String message) {
        log.info("MESSAGE recieved -> " + message);
        // messageRepo.addMessage(message);
    }

}
