package com.kafka.producer;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class MTKafkaProd {
	private static final String TOPIC_NAME = "";

	private Properties getKafkaProperties() {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducer");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return properties;
	}

	private KafkaProducer<Long, BookingEvents> createProducer() {
		return new KafkaProducer<>(getKafkaProperties());
	}

	public void submitRunner(int runner) {
		ExecutorService executor = Executors.newFixedThreadPool(runner);
		for (int x = 0; x < runner; x++) {
			executor.submit(() -> {
				createProducer().send(new ProducerRecord<>(TOPIC_NAME,
						new BookingEvents(UUID.randomUUID().toString(), "0", "0", "0", true)));
			});
		}
	}
}
