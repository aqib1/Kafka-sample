package com.test.main;
import java.io.IOException;

import client.kafka.com.KMTClient;

public class MainClass {

	public static void main(String[] args) {
		try {
			new KMTClient().init().readData();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
