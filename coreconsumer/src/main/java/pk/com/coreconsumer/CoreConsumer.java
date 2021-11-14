package pk.com.coreconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
//@EnableKafkaStreams 
@EnableScheduling
public class CoreConsumer {

	public static void main(String[] args) {
		SpringApplication.run(CoreConsumer.class, args);
	}

}
