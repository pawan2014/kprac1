package pk.com.kfexample.kfkalearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class MessageConsumer {
    LocalDateTime ts = LocalDateTime.now();
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

    @EventListener(condition = "event.listenerId.startsWith('NONDYNA-LST')")
    public void idleEventHandler(ListenerContainerIdleEvent event) {

        log.info("Idle Event Handler received message @ :: " + ts);
        log.info("Idle Event Handler received message @ :: " + LocalDateTime.now().isAfter(ts));
    }

}
