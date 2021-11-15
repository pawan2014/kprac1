package pk.com.monitor.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Monitor
 * -System
 * systemName
 * -topic
 * topicName
 * -Partition
 * number
 * startOffset
 * endOffset
 * currentOffset
 *
 * @author pk
 */

@Data
@Slf4j
public class MonitorMetaData {
    private String groupId;
    private List<SystemData> systemData = new ArrayList<>();

    public void checkProgress() {
        systemData.forEach(p -> p.getPartitionDataList().forEach(s -> {
            if (s.getEndOffset() == s.getCurrentOffset()) {
                s.setAllRecProcessed(true);
            }
        }));


    }

    public void updateCurrentOffset(String topicName, String partition, long endOffset) {
        Optional<SystemData> st = systemData.stream().filter(data -> data.getTopicName().equalsIgnoreCase(topicName)).findFirst();
        if (st.isPresent()) {
            Optional<PartitionData> sp = st.get().getPartitionDataList().stream().filter(data -> data.getPartitionName().equalsIgnoreCase(partition)).findFirst();
            if (sp.isPresent()) {
                sp.get().setCurrentOffset(endOffset);
                log.info("Updated current offset set for {} partition {} with offset {}", topicName, partition, endOffset);
            } else {
                log.error("Should not happen for {} partition {} with offset {}. partition info should already be added there", topicName, partition, endOffset);
            }
        } else {
            //TODO
            log.error("No System found with  name {}. will add it", topicName);
            // add System
        }
    }

    public void updateEndOffset(String topicName, String partition, long endOffset) {
        Optional<SystemData> st = systemData.stream().filter(data -> data.getTopicName().equalsIgnoreCase(topicName)).findFirst();
        if (st.isPresent()) {
            Optional<PartitionData> sp = st.get().getPartitionDataList().stream().filter(data -> data.getPartitionName().equalsIgnoreCase(partition)).findFirst();
            if (sp.isPresent()) {
                sp.get().setEndOffset(endOffset);
                log.info("Updated EndOffset set for {} partition {} with offset {}", topicName, partition, endOffset);
            } else {
                //add a new
                PartitionData partitionData = new PartitionData();
                partitionData.setPartitionName(partition);
                partitionData.setStartOffset(endOffset);

                st.get().getPartitionDataList().add(partitionData);
                log.info("New set for {} partition {} with offset {}", topicName, partition, endOffset);
            }
        } else {
            //TODO
            log.info("No System found with  name {}. will add it", topicName);
            // add System
        }
    }


}
