package pk.com.kfexample.kfkalearn.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class ProducerTopicConfig {

	@Value(value = "${spring.kafka.producer1.bootstrap-servers}")
	private String bootstrapAddress;

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return new DefaultKafkaProducerFactory<>(props);
	}

	@Bean
	public KafkaTemplate<String, String> customKafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
}
