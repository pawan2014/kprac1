package pk.com.kfexample.kfkalearn.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pk.com.kfexample.kfkalearn.MessageProducer;

@RestController
public class KafkaRestController {
    @Autowired
    private MessageProducer producer;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    // Send message to kafka
    @GetMapping("/send")
    public String sendMsg(@RequestParam("msg") String message) {
        producer.sendMessage(message);
        return "sent!";
    }

    @GetMapping(path = "/start")
    public String start() {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("DYNA-LST");
        listenerContainer.start();
        return "started listening";
    }

    @GetMapping(path = "/stop")
    public String stop() {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("DYNA-LST");
        listenerContainer.stop();
        return "stopped listening";
    }

}
