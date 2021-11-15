package pk.com.monitor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Synchronized;
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
import java.util.Map;

@Service
public class MonitorService {

    Map<String, MonitorMetaData> monitorMetaData = null;
    Path path = Paths.get("/home/pk/wrkspace/kprac1/data.json");
    //Map<String, MonitorMetaData>  monitorMetaData;
    @PostConstruct
    public void init(){
        try {
            byte[] st = Files.readAllBytes(path);
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            monitorMetaData =  mapper.readValue(st, new TypeReference<Map<String, MonitorMetaData>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            // if file now found exception then start with a new hashmap. this should happen only once
            monitorMetaData = new HashMap();
        }
    }
    public void updateProgress() {
        monitorMetaData.forEach((k,v)->{
            v.checkProgress();
        });
    }
    public MonitorMetaData updateCurrentProcessedOffset(String groupid, String topic, String partition, long offset) {
        MonitorMetaData data = null;
        if (monitorMetaData.containsKey(groupid)) {
            // only update the end offset
            data = monitorMetaData.get(groupid);
            data.updateCurrentOffset(topic, partition,offset);
            monitorMetaData.put(groupid, data);
            writeJsonConfig();
        }
        return data;
    }

    public MonitorMetaData update(String groupid, String systemName, String topic, String partition, long offset) {
        MonitorMetaData data = null;
        if (monitorMetaData.containsKey(groupid)) {
            // only update the end offset
            data = monitorMetaData.get(groupid);
            data.updateEndOffset(systemName, partition,offset);
            monitorMetaData.put(groupid, data);
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

            monitorMetaData.put(groupid, data);
        }

        writeJsonConfig();
        // System.out.println(metaDataJson);

        return data;

    }

    // TODO Concurent thread class can happen, fix this in future
    private void writeJsonConfig() {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String metaDataJson = null;
        try {
            metaDataJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(monitorMetaData);

            Files.write(path,metaDataJson.getBytes());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, MonitorMetaData> getMetadata() {
        return monitorMetaData;
    }

}
