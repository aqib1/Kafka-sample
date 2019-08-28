package client.kafka.com;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.PrivilegedExceptionAction;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormatCounter;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import kafka.serializer.StringDecoder;

public class KMTClient {
	private static final String URL_HDFS = "hdfs://sandbox-hdp.hortonworks.com:8020/user/maria_dev";
	private static final int BATCH_INTERVAL_DURATION = 5000;
	private SparkConf sparkConf;
	private JavaSparkContext sparkContext;
	private JavaStreamingContext javaStreamingContext;
	private JavaPairInputDStream<String, String> kafkaSparkPairInputDstream;
	private static String TOPIC_NAME = "BookingTopic";
	private Set<String> topics = Collections.singleton(TOPIC_NAME);
	private static final int BROKER_PORT_NUMBER = 6667;
	private static final String BROKER_DOMAIN_NAME = "sandbox-hdp.hortonworks.com";
	private static final String CLIENT_ID = "KMTClient";
	private UserGroupInformation ugi = UserGroupInformation.createRemoteUser("maria_dev");
	private FileSystem fileSystem;

	public KMTClient init() throws IOException, InterruptedException {
		sparkConf = new SparkConf().setAppName("KMTClient").setMaster("local[*]");
		sparkContext = new JavaSparkContext(sparkConf);
		javaStreamingContext = new JavaStreamingContext(sparkContext, new Duration(BATCH_INTERVAL_DURATION));
		kafkaSparkPairInputDstream = KafkaUtils.createDirectStream(javaStreamingContext, String.class, String.class,
				StringDecoder.class, StringDecoder.class, getKafkaParams(), topics);
		ugi.doAs(new PrivilegedExceptionAction<Void>() {

			@Override
			public Void run() throws Exception {
				Configuration conf = new Configuration();
                conf.set("fs.defaultFS", URL_HDFS);
                conf.set("hadoop.job.ugi", "maria_dev");
                fileSystem = FileSystem.get(conf);
				return null;
			}
		});
		return this;
	}

	public void readData() throws InterruptedException {
		JavaDStream<String> javaDStream = kafkaSparkPairInputDstream.map((tuple) -> {
			return tuple._2();
		});
		javaDStream.foreachRDD(rdd -> {
			List<String> data = rdd.collect();
			String name = LocalDateTime.now().toString();
			name = name.replace(":", "_").replace(".", "").replace("-", "");
			Path outPutPath = new Path(URL_HDFS+"/"+name+"_temp.txt");
			OutputStream out = fileSystem.create(outPutPath);
			data.stream().forEach(x -> {
				try(OutputStreamWriter write = new OutputStreamWriter(out)) {
					write.write(x);
					write.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			});
			out.close();
		});
		javaStreamingContext.start();
		javaStreamingContext.awaitTermination();

	}

	private Map<String, String> getKafkaParams() {
		Map<String, String> map = new HashMap<>();
		map.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_DOMAIN_NAME + ":" + BROKER_PORT_NUMBER);
		map.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
		map.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		map.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return map;
	}
}
