package pk.com.monitor.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pk.com.monitor.model.MonitorMetaData;
import pk.com.monitor.service.MonitorService;

import java.util.HashMap;

@RestController
public class KafkaRestController {

    @Autowired
    private MonitorService ms;

    // Test Monitor service
    @GetMapping("/monitor/service")
    public MonitorMetaData monitorUpdate(@RequestParam("group") String group,
                                         @RequestParam("system") String system,
                                         @RequestParam(value = "topic", defaultValue = "test topic") String topic,
										 @RequestParam(value = "partition") String partition,
                                         @RequestParam("offset") long offset)
                                         {
        return ms.update(group, topic,topic,partition, offset);

    }

    // Get All metadata
    @GetMapping("/monitor")
    public HashMap<String, MonitorMetaData> monitorUpdate() {
        return ms.getMetadata();

    }


}
