package pk.com.monitor.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
public class ConsumerTopicConfig {
	private Logger log = LoggerFactory.getLogger(ConsumerTopicConfig.class);

	@Value(value = "${spring.kafka.producer1.bootstrap-servers}")
	private String bootstrapAddress;

	/*@Value(value = "${myapp.kafka.topic}")
	private String topicName;*/

	private String groupId = "BATCH-GRP";

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		log.info("->bootstrapAddress=" + bootstrapAddress);
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "2");
		return new DefaultKafkaConsumerFactory<>(props);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> customKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		//factory.setConcurrency(3);
		factory.setBatchListener(true);
		return factory;
	}

	/*
	 * To create multiple consumer factory do it like this, where
	 * someConsumerFactory is injected into the someKaListenerContainerFactory as
	 * factory.setConsumerFactory(someConsumerFactory) public
	 * ConsumerFactory<String, Stirng> someConsumerFactory() public
	 * ConcurrentKafkaListenerContainerFactory<String, String>
	 * someKaListenerContainerFactory()
	 */
	
	
	
}
