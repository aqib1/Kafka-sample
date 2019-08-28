import com.kafka.producer.MTKafkaProd;

public class MainClass {

	public static void main(String[] args) {
		try {
			if (args.length == 0)
				new MTKafkaProd().submitRunner(1);
			else
				new MTKafkaProd().submitRunner(Integer.parseInt(args[0]));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
