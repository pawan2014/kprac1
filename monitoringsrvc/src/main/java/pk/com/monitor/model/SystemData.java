package pk.com.monitor.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Consider system name same as topic name
 */
@Data
public class
SystemData {
	private String systemName;
	private String topicName;
	private boolean endOfDay=false;
	private boolean locked;
	List<PartitionData> partitionDataList= new ArrayList<>();


}
