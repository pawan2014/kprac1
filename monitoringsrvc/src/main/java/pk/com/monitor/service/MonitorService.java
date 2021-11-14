package pk.com.monitor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pk.com.monitor.model.MonitorMetaData;
import pk.com.monitor.model.PartitionData;
import pk.com.monitor.model.SystemData;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@Service
public class MonitorService {

    HashMap<String, MonitorMetaData> m = new HashMap();
    Path path = Paths.get("/home/pk/wrkspace/kprac1/data.json");
    MonitorMetaData monitorMetaData;
    @PostConstruct
    public void init(){
        try {
            byte[] st = Files.readAllBytes(path);
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            monitorMetaData = mapper.readValue(st.toString(),MonitorMetaData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MonitorMetaData update(String groupid, String systemName, String topic, String partition, long offset) {
        MonitorMetaData data = null;
        if (m.containsKey(groupid)) {
            // only update the end offset
            data = m.get(groupid);
            data.updateEndOffset(systemName, partition,offset);
            m.put(groupid, data);
        } else {

            PartitionData partitionData = new PartitionData();
            partitionData.setPartitionName(partition);
            partitionData.setStartOffset(offset);
            //
            SystemData systemData = new SystemData();
            systemData.setSystemName(topic);
            systemData.setTopicName(topic);
            systemData.getPartitionDataList().add(partitionData);
            // add as a new entry
            data = new MonitorMetaData();
            data.setGroupId(groupid);
            data.getSystemData().add(systemData);

            m.put(groupid, data);
        }

        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String user = null;
        try {
            user = mapper.writeValueAsString(m);

            Files.write(path,user.getBytes());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(user);

        return data;

    }

    public HashMap<String, MonitorMetaData> getMetadata() {
        return m;
    }

}
