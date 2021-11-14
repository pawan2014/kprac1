package pk.com.kfexample.kfkalearn.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

@Service
public class MonitorService {

	HashMap<String,MonitorMetaData> m = new HashMap();
	
	public MonitorMetaData update(String groupid, String systemName, long l, long m2) {
		MonitorMetaData data=null ;
		if(m.containsKey(groupid)) {
			// only update the end offset
			data = m.get(groupid);
			data.getSystemData().setEndOffset(m2);
			m.put(groupid, data);
		}else {
			// add as a new entry
			data = new MonitorMetaData();
			data.setGroupid(groupid);
			//
			SystemData systemData = new SystemData();
			systemData.setSystemName(systemName);
			systemData.setStartOffset(l);
			systemData.setEndOffset(0);
			
			data.setSystemData(systemData);
			m.put(groupid, data);
		}
		
		return data;

	}
	
	public HashMap<String, MonitorMetaData> getMetadata() {
		return m;
	}

}
