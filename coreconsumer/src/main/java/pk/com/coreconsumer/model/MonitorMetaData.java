package pk.com.coreconsumer.model;

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
	@Override
	public String toString() {
		return "MonitorMetaData [groupid=" + groupid + ", systemData=" + systemData + "]";
	}
	
}
