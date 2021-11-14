package pk.com.kfexample.kfkalearn.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pk.com.kfexample.kfkalearn.MessageProducer;

@Component
public class MetaInfoDumper {

	@Autowired
	MonitorService msrv;

	@Autowired
	private MessageProducer producer;

	@Scheduled(fixedRate = 5000)
	public void fixedRateSch() {
		System.out.println("Fixed Rate scheduler:: " + msrv.getMetadata());
	}

	@Scheduled(fixedRate = 1000)
	public void produceData() {
		producer.sendMessage("grp1;SYS1;" + UUID.randomUUID().toString());
		// System.out.println("Fixed Rate scheduler:: " + msrv.getMetadata());
	}

}
