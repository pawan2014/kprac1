package pk.com.monitor.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pk.com.monitor.model.MonitorMetaData;
import pk.com.monitor.service.MonitorService;

import java.util.Map;

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
        return ms.createMetaNode(group, topic,topic,partition, offset);

    }

    // Get All metadata
    @GetMapping("/monitor")
    public Map<String, MonitorMetaData> monitorUpdate() {
        return ms.getMetadata();

    }

    @GetMapping("/monitor/currentOffset/update")
    public MonitorMetaData updateCurrentOffset(@RequestParam("group") String group,
                                         @RequestParam(value = "topic", defaultValue = "test topic") String topic,
                                         @RequestParam(value = "partition") String partition,
                                         @RequestParam("offset") long offset)
    {
        return ms.updateCurrentProcessedOffset(group,topic,partition, offset);

    }

    @GetMapping("/monitor/updateeod")
    public Map<String, MonitorMetaData> updateSystemEOD(@RequestParam("group") String group,
                                                        @RequestParam(value = "topic") String topic,
                                                        @RequestParam(value = "eod") boolean eod
                                               )
    {
        return ms.updateSystemEOD(group,topic,eod);

    }




}
