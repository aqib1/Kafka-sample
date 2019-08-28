package com.kafka.producer;

import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class MTKafkaProd {
	private static String TOPIC_NAME = "BookingTopic";
	private static final int MAX_NUMBER_OF_MESSAGES = 50000;
	private static final int BROKER_PORT_NUMBER = 6667;
	private static final String BROKER_DOMAIN_NAME = "sandbox-hdp.hortonworks.com";
	private static final String CLIENT_ID = "MTKafkaProd";

	public MTKafkaProd init(String topic) {
		TOPIC_NAME = topic;
		return this;

	}

	private Properties getKafkaProperties() {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_DOMAIN_NAME + ":" + BROKER_PORT_NUMBER);
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return properties;
	}

	private KafkaProducer<String, String> createProducer() {
		return new KafkaProducer<>(getKafkaProperties());
	}

	private Callback getCallBack(final long startTime) {
		return (metadata, e) -> {
			if (!Objects.isNull(e)) {
				e.printStackTrace();
			} else {

				System.out.println("[" + metadata.toString() + "]" + " Time Elapsed => "
						+ (System.currentTimeMillis() - startTime));
			}
		};
	}

	public MTKafkaProd submitRunner(int runner) throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(runner);

		for (int x = 0; x < runner; x++) {
			executorService.execute(() -> {
				Random random = new Random();
				KafkaProducer<String, String> prod = createProducer();
				for (int y = 0; y < MAX_NUMBER_OF_MESSAGES; y++) {
					try {
						prod.send(
								new ProducerRecord<String, String>(TOPIC_NAME,
										DataHelper.getInstance().getData()
												.get(random.nextInt(DataHelper.getInstance().getData().size()))),
								getCallBack(System.currentTimeMillis()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				prod.close();
			});
		}
		executorService.shutdown();
		executorService.awaitTermination(1, TimeUnit.DAYS);

		return this;
	}
}
