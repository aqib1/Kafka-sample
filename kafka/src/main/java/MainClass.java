import com.kafka.producer.MTKafkaProd;

public class MainClass {
	
	public static void main(String[] args) {
		try {
			new MTKafkaProd().submitRunner(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
