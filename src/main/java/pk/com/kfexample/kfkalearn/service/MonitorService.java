package pk.com.kfexample.kfkalearn.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

@Service
public class MonitorService {

	HashMap<String,MonitorMetaData> m = new HashMap();
	
	public MonitorMetaData update(String groupid, String systemName, Integer startoffset, Integer endoffset) {
		MonitorMetaData data=null ;
		if(m.containsKey(groupid)) {
			// only update the end offset
			data = m.get(groupid);
			data.getSystemData().setEndOffset(endoffset);
			m.put(groupid, data);
		}else {
			// add as a new entry
			data = new MonitorMetaData();
			data.setGroupid(groupid);
			//
			SystemData systemData = new SystemData();
			systemData.setSystemName(systemName);
			systemData.setStartOffset(startoffset);
			systemData.setEndOffset(endoffset);
			
			data.setSystemData(systemData);
			m.put(groupid, data);
		}
		
		return data;

	}
	
	public HashMap<String, MonitorMetaData> getMetadata() {
		return m;
	}

}
