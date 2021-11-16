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

		String[] names = { "Aerminator", "BSlicer","CNinja", "Dcow", "LOI","POP","SAM","GOL","MOL"
				,"POL","DHOL","BACK","love","santa","khat","join","party"};
		//int[] parNumber = { 0,1,2,3,4,5};
		double randomNumberTo5 = (Math.random() * names.length);

		String name = names[(int) randomNumberTo5];
		//int pNumber = parNumber[(int) randomNumberTo5];

		producer.sendMessage(name,0,"grp1;SYS1;" + UUID.randomUUID().toString());
	}

}
