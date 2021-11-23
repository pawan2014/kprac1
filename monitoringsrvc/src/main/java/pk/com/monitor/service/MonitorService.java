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
    public static final String MY_S1 = "MY_BL";
    public static final String MY_S2 = "MY-TEST-TOPIC_1";
    public static final String MY_S3 = "MY-TEST-TOPIC_2";


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
        systemData.setSystemName(MY_S1);
        systemData.setTopicName(MY_S1);
        systemData.setEndOfDay(false);
        systemData.setLocked(true);

        // add as a new entry
        MonitorMetaData data = new MonitorMetaData();
        data.setGroupId(GROUP_ID);
        data.getSystemData().add(systemData);

        return data;
    }

    public void calculateEOD() {
        updateS2(metaDataMap);
        writeJsonConfig();
    }
    synchronized public void updateS2(Map<String, MonitorMetaData> metaDataMap) {
        MonitorMetaData  blMeta = metaDataMap.get(GROUP_ID);
        Optional<SystemData> s2SystemData = blMeta.getSystemData().stream().filter(p->p.getSystemName().equalsIgnoreCase(MY_S2)).findFirst();
        if(s2SystemData.isPresent()){
            SystemData  data = s2SystemData.get();
            data.getPartitionDataList().stream().forEach(p->{
                // for that system check all parition and set flag
                if (p.getEndOffset() == p.getCurrentOffset()) {
                    p.setAllRecProcessed(true);
                } else {
                    // this is in case producer is still producing and consumer is very fast
                    p.setAllRecProcessed(false);
                }
            });
            // Mark only is S1 is EOD
            Optional<SystemData> s1SystemData = blMeta.getSystemData().stream().filter(p->p.getSystemName().equalsIgnoreCase(MY_S1)).findFirst();
            long totalCount = data.getPartitionDataList().stream().count();
            long processedCount = data.getPartitionDataList().stream().filter(p -> p.isAllRecProcessed()).count();
            if (totalCount == processedCount && s1SystemData.get().isEndOfDay()) {
                data.setEndOfDay(true);
            } else {
                //data.setEndOfDay(false);
            }
        }




    }

    public Map<String, MonitorMetaData> updateSystemEOD(String groupId, String systemName, boolean flag) {
        if (metaDataMap.containsKey(groupId)) {
            MonitorMetaData metaData = metaDataMap.get(groupId);
            metaData.getSystemData().stream().forEach(p -> {
                if (p.getSystemName().equalsIgnoreCase(systemName)) {
                    p.setEndOfDay(flag);
                }
            });
            writeJsonConfig();
        } else {
            System.out.println("No group found");
        }

        return metaDataMap;
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
