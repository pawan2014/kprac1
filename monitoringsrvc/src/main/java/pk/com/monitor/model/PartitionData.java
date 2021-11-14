package pk.com.monitor.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j

public class PartitionData {
	String partitionName;
	private long startOffset;
	private long endOffset;
	private long currentOffset;
	boolean allRecProcessed=false;

	public void process(){
		if(endOffset==currentOffset){
			log.info("parition processing completed="+endOffset+"--"+currentOffset);
			allRecProcessed=true;
		}
	}
}
