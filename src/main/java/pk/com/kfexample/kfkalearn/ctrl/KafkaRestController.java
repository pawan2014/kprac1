package pk.com.kfexample.kfkalearn.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pk.com.kfexample.kfkalearn.MessageProducer;

@RestController
public class KafkaRestController {
	 @Autowired
     private MessageProducer producer;
	
	// Send message to kafka
	@GetMapping("/send")
	public String sendMsg(@RequestParam("msg") String message) {
		producer.sendMessage(message);
		return "sent!";
	}

}
