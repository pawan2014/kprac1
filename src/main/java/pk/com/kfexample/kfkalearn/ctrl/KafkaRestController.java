package pk.com.kfexample.kfkalearn.ctrl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pk.com.kfexample.kfkalearn.MessageProducer;
import pk.com.kfexample.kfkalearn.service.MonitorMetaData;
import pk.com.kfexample.kfkalearn.service.MonitorService;

@RestController
public class KafkaRestController {
	@Autowired
	private MessageProducer producer;

	@Autowired
	private MonitorService ms;

	// Send message to kafka
	@GetMapping("/send")
	public String sendMsg(@RequestParam("msg") String message) {
		producer.sendMessage(message);
		return "sent!";
	}

	// Test Monitor service
	@GetMapping("/monitor/service")
	public MonitorMetaData monitorUpdate(@RequestParam("group") String group, @RequestParam("system") String system,
			@RequestParam("start") Integer startOffset, @RequestParam("end") Integer endOffset) {
		return ms.update(group, system, startOffset, endOffset);

	}

	// Get All metadata
	@GetMapping("/monitor")
	public HashMap<String, MonitorMetaData> monitorUpdate() {
		return ms.getMetadata();

	}

}
