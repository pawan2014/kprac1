package pk.com.kfexample.kfkalearn;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka

public class KConfig {
    @Value(value = "${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(1);
        /*
         * Here we are setting idle interval. When the listener is idle for below mentioned idle
         * time interval in milliseconds an event will
         * be published.
         */
        factory.getContainerProperties().setIdleEventInterval(1000L);
        factory.getContainerProperties().setPollTimeout(3000);
        /*
        Notice that there are two callbacks when partitions are revoked. The first is called immediately.
        The second is called after any pending offsets are committed. This is useful if you wish to maintain
        offsets in some external repository, as the following example shows:
         */
        factory.getContainerProperties().setConsumerRebalanceListener(new ConsumerAwareRebalanceListener() {
            @Override
            public void onPartitionsRevokedBeforeCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
                // acknowledge any pending Acknowledgments (if using manual acks)
                System.out.println("onPartitions Revoked BeforeCommit" + consumer.groupMetadata());
            }

            @Override
            public void onPartitionsRevokedAfterCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
                System.out.println("onPartitions Revoked AfterCommit" + consumer.groupMetadata());
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.println("pk partitions assigned" + partitions);
                // consumer.seek(partition, offsetTracker.getOffset() + 1);
                // ...
            }
        });
        return factory;
    }

    @Bean
    public ConsumerFactory<Integer, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "5");
        return props;
    }


}
