package pk.com.kfexample.kfkalearn.config;

import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

@Configuration
public class StreamConfig {

	@Value(value = "${myapp.kafka.topic}")
	private String topicName;

	/*
	 * you can provide a customized configuration for your Kafka Streams
	 * application, providing application ID, bootstrap server connection, details
	 * of a Kafka broker, etc
	 */
	@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
	public KafkaStreamsConfiguration kStreamsConfigs() {
		return new KafkaStreamsConfiguration(Map.of(StreamsConfig.APPLICATION_ID_CONFIG, "testStreams",
				StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092", StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
				Serdes.String().getClass().getName(), StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
				Serdes.String().getClass().getName(), StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
				WallclockTimestampExtractor.class.getName()));
	}

	@Bean
	public KStream<String, String> kStream(StreamsBuilder kStreamBuilder) {
		KStream<String, String> stream = kStreamBuilder.stream(topicName);
		stream.foreach((key, value) -> {
			System.out.println("================");
			System.out.println(value);
		});

		final Serde<String> stringSerde = Serdes.String();
		KTable<String, Long> wordCounts = stream.groupBy((key, value) -> value, Grouped.with(stringSerde, stringSerde))
				.count();

		wordCounts.toStream().foreach((key, value) -> {
			System.out.println("Count:"+key+"-"+value);
		});
		// stream.print(Printed.toSysOut());
		return stream;
	}
}
