package BestPriceFinder.bestpricefindershop;

import java.util.concurrent.Future;

public class ShopExe {

	public static void main(String[] args) {

		/*
		 * Shop shop = new Shop(); double item1price = shop.getPrice("Mixa");
		 * System.out.println(item1price);
		 * 
		 * double item2price = shop.getPrice("Water"); System.out.println(item2price);
		 */
		Shop shop = new Shop("my favorite product");
		long start = System.nanoTime();
		Future<Double> futurePrice = shop.getPriceAsync();
		long invocationTime = ((System.nanoTime() - start)/ 1_000_000);
		System.out.println("Invocation returned after " + invocationTime + " msecs");
		
		//do some more tasks, like querying other shops
		//doSomethingElse();
		
		//while the price of the product is being calculated
		try {
			double price = futurePrice.get();
			System.out.println("Price is " + price);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		long retrievalTime = ((System.nanoTime() - start)/ 1_000_000);
		System.out.println("Price returned after " + retrievalTime + " msecs" );
	}

}











