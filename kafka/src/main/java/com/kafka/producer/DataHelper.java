package com.kafka.producer;

import java.util.List;
import java.util.UUID;

public class DataHelper {

	private List<String> data = List.of(UUID.randomUUID().toString() + " 0 " + " 0 " + " 0 " + true,
			UUID.randomUUID().toString() + " 0 " + " 0 " + " 0 " + true,
			UUID.randomUUID().toString() + " 0 " + " 0 " + " 0 " + true,
			UUID.randomUUID().toString() + " 0 " + " 0 " + " 0 " + true,
			UUID.randomUUID().toString() + " 0 " + " 0 " + " 0 " + true,
			UUID.randomUUID().toString() + " 0 " + " 0 " + " 0 " + true,
			UUID.randomUUID().toString() + " 0 " + " 0 " + " 0 " + true,
			UUID.randomUUID().toString() + " 0 " + " 0 " + " 0 " + true,
			UUID.randomUUID().toString() + " 0 " + " 0 " + " 0 " + true,
			UUID.randomUUID().toString() + " 0 " + " 0 " + " 0 " + true);

	public List<String> getData() {
		return data;
	}

	private DataHelper() {

	}

	public static DataHelper getInstance() {
		return DataHelperInitializationOnDemand.DATA_HELPER;
	}

	private static class DataHelperInitializationOnDemand {
		private static final DataHelper DATA_HELPER = new DataHelper();

		private DataHelperInitializationOnDemand() {

		}
	}
}
