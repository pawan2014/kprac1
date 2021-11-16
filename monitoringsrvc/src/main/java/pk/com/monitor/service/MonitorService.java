package pk.com.monitor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.Map;
import java.util.Optional;

@Service
public class MonitorService {
    public static final String GROUP_ID = "FORNOW-HARDCODED";
    public static final String MY_BL = "MY_BL";

    Map<String, MonitorMetaData> metaDataMap = null;
    Path path = Paths.get("/home/pk/wrkspace/kprac1/data.json");


    @PostConstruct
    public void init() {
        try {
            byte[] st = Files.readAllBytes(path);
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            metaDataMap = mapper.readValue(st, new TypeReference<Map<String, MonitorMetaData>>() {
            });

        } catch (IOException e) {
            e.printStackTrace();
            // if file now found exception then start with a new hashmap. this should happen only once
            metaDataMap = new HashMap();
            metaDataMap.put(GROUP_ID, seedWithDummySystemName());
        }
    }

    private MonitorMetaData seedWithDummySystemName() {
        SystemData systemData = new SystemData();
        systemData.setSystemName(MY_BL);
        systemData.setTopicName(MY_BL);
        systemData.setEndOfDay(false);
        systemData.setLocked(true);

        // add as a new entry
        MonitorMetaData data = new MonitorMetaData();
        data.setGroupId(GROUP_ID);
        data.getSystemData().add(systemData);

        return data;
    }

    public void updateProgress() {
        metaDataMap.forEach((k, v) -> {
            v.checkProgress();
        });
    }

    public void updateSystemEOD(String groupId, String systemName, boolean flag) {
        if (metaDataMap.containsKey(groupId)) {
            MonitorMetaData metaData = metaDataMap.get(groupId);
            metaData.getSystemData().stream().forEach(p -> {
                if (p.getSystemName().equalsIgnoreCase(systemName)) {
                    p.setEndOfDay(flag);
                }
            });

        } else {
            System.out.println("No group found");
        }
    }

    public MonitorMetaData updateCurrentProcessedOffset(String groupid, String topic, String partition, long offset) {
        MonitorMetaData data = null;
        if (metaDataMap.containsKey(groupid)) {
            // only update the end offset
            data = metaDataMap.get(groupid);
            data.updateCurrentOffset(topic, partition, offset);
            metaDataMap.put(groupid, data);
            writeJsonConfig();
        }
        return data;
    }

    public MonitorMetaData createMetaNode(String groupid, String systemName, String topic, String partition, long offset) {
        MonitorMetaData data = null;
        if (metaDataMap.containsKey(groupid)) {
            data = metaDataMap.get(groupid);
            Optional<SystemData> s = data.getSystemData().stream().filter(p -> p.getSystemName().equalsIgnoreCase(systemName)).findFirst();
            if (s.isPresent()) {
                data.updateEndOffset(systemName, partition, offset);
            } else {
                PartitionData partitionData = new PartitionData();
                partitionData.setPartitionName(partition);
                partitionData.setStartOffset(offset);
                //
                SystemData systemData = new SystemData();
                systemData.setSystemName(topic);
                systemData.setTopicName(topic);
                systemData.getPartitionDataList().add(partitionData);

                data.getSystemData().add(systemData);

            }
            metaDataMap.put(groupid, data);
            writeJsonConfig();
        }
        return data;

    }

    // TODO Concurent thread class can happen, fix this in future
    synchronized private void writeJsonConfig() {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String metaDataJson = null;
        try {
            metaDataJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(metaDataMap);

            Files.write(path, metaDataJson.getBytes());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, MonitorMetaData> getMetadata() {
        return metaDataMap;
    }

}
