package pk.com.kfexample.producer;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MetaInfoDumper {

	@Autowired
	private MessageProducer producer;

	@Scheduled(fixedRate = 1000)
	public void produceData() {
		String[] names = { "Terminator", "Slicer","Ninja", "cow", "Robot", "littlegirl" };
		String name = names[(int) (Math.random() * names.length)];
		producer.sendMessage(name,"grp1;SYS1;" + UUID.randomUUID().toString());
	}

}
