package pk.com.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MetaInfoDumper {

	@Autowired
	MonitorService msrv;

	
	@Scheduled(fixedRate = 5000)
	public void fixedRateSch() {
		System.out.println("Fixed Rate scheduler:: " + msrv.getMetadata());
	}

	@Scheduled(fixedRate = 1000)
	public void updateProgress() {
		System.out.println("Updating progress...");
		msrv.calculateEOD();
	}


}
