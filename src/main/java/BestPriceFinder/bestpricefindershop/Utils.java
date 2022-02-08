package BestPriceFinder.bestpricefindershop;

import java.util.Random;

public class Utils {

	private static final Random random = new Random();

	// in the real-world remote services delay will vary between several seconds
	// and its depicted here using randomDelay instead of the ordinaryDelay

	public static void delay() {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	// delay btwn 0.5 and 2.5s
	public static void randomDelay() {
		int delay = 500 + random.nextInt(2000);
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e2) {
			throw new RuntimeException(e2);
		}
	}

	public static double format(double value) {
		return value;
	}
}
