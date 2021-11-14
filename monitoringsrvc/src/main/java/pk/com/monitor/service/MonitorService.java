package pk.com.monitor.service;

import org.springframework.stereotype.Service;
import pk.com.monitor.model.MonitorMetaData;
import pk.com.monitor.model.PartitionData;
import pk.com.monitor.model.SystemData;

import java.util.HashMap;

@Service
public class MonitorService {

    HashMap<String, MonitorMetaData> m = new HashMap();

    public MonitorMetaData update(String groupid, String systemName, String topic, String partition, long startOffset, long endOffset) {
        MonitorMetaData data = null;
        if (m.containsKey(groupid)) {
            // only update the end offset
            data = m.get(groupid);
            data.updateEndOffset(systemName, partition, endOffset);
            m.put(groupid, data);
        } else {

            PartitionData partitionData = new PartitionData();
            partitionData.setPartitionName(partition);
            partitionData.setCurrentOffset(startOffset);
            //
            SystemData systemData = new SystemData();
            systemData.setSystemName(systemName);
            systemData.setTopicName(topic);
            systemData.getPartitionDataList().add(partitionData);
            // add as a new entry
            data = new MonitorMetaData();
            data.setGroupId(groupid);
            data.getSystemData().add(systemData);

            m.put(groupid, data);
        }

        return data;

    }

    public HashMap<String, MonitorMetaData> getMetadata() {
        return m;
    }

}
