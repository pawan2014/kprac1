package pk.com.coreconsumer.ctrl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pk.com.coreconsumer.model.MonitorMetaData;

@RestController
public class KafkaRestController {


	// Not needed for now but kept as a placeholder
	@GetMapping("/monitor/service")
	public MonitorMetaData monitorUpdate(@RequestParam("group") String group, @RequestParam("system") String system,
			@RequestParam("start") Integer startOffset, @RequestParam("end") Integer endOffset) {
		return null;

	}


}
