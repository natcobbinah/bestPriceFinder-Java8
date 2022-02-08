package BestPriceFinder.bestpricefindershop;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Shopfinalexe {

	public static void main(String[] args) {

		List<Shop> shops = Arrays.asList(new Shop("BestPrice"), new Shop("LetsSaveBig"), new Shop("MyFavoriteShop"),
				new Shop("BuyItAll"));

		long start = System.nanoTime();
		CompletableFuture[] futures = Shop.findPricesStream(shops, "myPhone27s")
				.map(f -> f.thenAccept(s -> System.out
						.println(s + " (done in " + ((System.nanoTime() - start) / 1_000_000) + " msecs)")))
				.toArray(size -> new CompletableFuture[size]);

		CompletableFuture.allOf(futures).join();
		System.out.println("All shops have now responded in " + ((System.nanoTime() - start) / 1_000_000 + " msecs"));

	}
}
