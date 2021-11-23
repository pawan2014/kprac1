package pk.com.monitor.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.Map;

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
	public KStream<String, String> kStream2(StreamsBuilder kStreamBuilder) {

		KStream<String, String> stream = kStreamBuilder.stream("MY-TEST-TOPIC_2");
		stream.foreach((key, value) -> {
			System.out.println("**"+value);
		});
	return  stream;
	}
	@Bean
	public Topology kStream(StreamsBuilder kStreamBuilder) {
		//KStream<String, String>
		//KStream<String, String> stream = kStreamBuilder.stream(topicName);
		//stream.foreach((key, value) -> {
			//System.out.println("================");
			//System.out.println(value);
		//});

		Topology topology = kStreamBuilder.build();
		topology.addSource("SOURCE", topicName)
				.addProcessor("PROCESS", new ProcessorSupplier() {
					@Override
					public Processor get() {
						return new Processor() {
							@Override
							public void init(ProcessorContext context) {

							}

							@Override
							public void process(Object key, Object value) {
								System.out.println("inside the processor="+key+"===="+value);
							}

							@Override
							public void close() {

							}
						};
					}
				},"SOURCE")
				.addSink("SINK3", "MY-TEST-TOPIC_2", "PROCESS");


		/*
		Commented for now as this is of no used in this testing
		final Serde<String> stringSerde = Serdes.String();
		KTable<String, Long> wordCounts = stream.groupBy((key, value) -> value, Grouped.with(stringSerde, stringSerde))
				.count();

		wordCounts.toStream().foreach((key, value) -> {
			System.out.println("Count:"+key+"-"+value);
		});
		// stream.print(Printed.toSysOut());
		*/
		//return stream;
		return topology;
	}

	private class ProcessorA {
	}
}
