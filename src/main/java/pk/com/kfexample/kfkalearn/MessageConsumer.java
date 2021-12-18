package pk.com.kfexample.kfkalearn;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class MessageConsumer {
    LocalDateTime ts = LocalDateTime.now();
    private Logger log = LoggerFactory.getLogger(MessageConsumer.class);

    //@KafkaListener(id = "NONDYNA-LST", topics = "${myapp.kafka.topic}", groupId = "ex9-1")
    public void listen(ConsumerRecord<?, ?> record) {
        log.info("MESSAGE received on-> " + record.partition() + " at offset=" + record.offset());
    }


}
