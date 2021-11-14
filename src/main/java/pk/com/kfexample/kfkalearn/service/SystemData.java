package pk.com.kfexample.kfkalearn.service;

public class SystemData {
	private String systemName;
	private String topicName;
	private long startOffset;
	private long endOffset;
	
	
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public long getStartOffset() {
		return startOffset;
	}
	public void setStartOffset(long startOffset) {
		this.startOffset = startOffset;
	}
	public long getEndOffset() {
		return endOffset;
	}
	public void setEndOffset(long m2) {
		this.endOffset = m2;
	}
	@Override
	public String toString() {
		return "SystemData [systemName=" + systemName + ", startOffset=" + startOffset + ", endOffset=" + endOffset
				+ "]";
	}
	
}
