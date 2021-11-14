package pk.com.kfexample.kfkalearn.service;

import lombok.Data;

@Data
public class MonitorMetaData {

	private String groupid;
	private SystemData systemData;
	
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public SystemData getSystemData() {
		return systemData;
	}
	public void setSystemData(SystemData systemData) {
		this.systemData = systemData;
	}
	
}
