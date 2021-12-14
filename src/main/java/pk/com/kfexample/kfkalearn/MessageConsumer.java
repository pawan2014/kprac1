package pk.com.kfexample.kfkalearn;

import org.apache.kafka.clients.consumer.ConsumerRecord;
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
    }

    @KafkaListener(id = "NONDYNA-LST", topics = "${myapp.kafka.topic}", groupId = "ex9-1")
    public void listen(ConsumerRecord<?, ?> record) {
        log.info("MESSAGE received on-> " + record.partition() + " at offset=" + record.offset());
    }

    @EventListener(condition = "event.listenerId.startsWith('NONDYNA-LST')")
    public void idleEventHandler(ListenerContainerIdleEvent event) {
        log.info("Idle Event Handler received message @ :: " + ts);
        //log.info("Idle Event Handler received message @ :: " + LocalDateTime.now().isAfter(ts));
    }

}
