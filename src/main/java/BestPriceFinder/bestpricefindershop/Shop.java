package BestPriceFinder.bestpricefindershop;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import BestPriceFinder.bestpricefindershop.Discount.Code;

public class Shop {

	Random random = new Random();

	String product;

	public String getProduct() {
		return product;
	}

	Shop(String product) {
		this.product = product;
	}

	// creating an asynchronous api
	public Future<Double> getPriceAsync() {
		CompletableFuture<Double> futurePrice = new CompletableFuture<>();
		new Thread(() -> {
			try {
				double price = calculatePrice(product);
				futurePrice.complete(price);
			} catch (Exception e) {
				futurePrice.completeExceptionally(e);
			}
		}).start();
		return futurePrice;
	}

	// even a better api fewer lines of code
	public Future<Double> getPriceAsync2() {
		return CompletableFuture.supplyAsync(() -> calculatePrice(product));
	}

	// findPrices
	public static List<String> findPrices(List<Shop> shops, String product) {
		return shops.parallelStream()
				.map(shop -> String.format("%s price is %.2f", shop.getProduct(), shop.getPrice(product)))
				.collect(Collectors.toList());
	}

	// findPrices using completable futures
	public static List<String> findPricesFuture(List<Shop> shops, String product) {
		// call executor method to schedule effective use of threads in the system
		Executor executor = Shop.executor(shops);

		List<CompletableFuture<String>> priceFutures = shops.stream()
				.map(shop -> CompletableFuture
						.supplyAsync(() -> shop.getProduct() + " price is " + shop.getPrice(product), executor))
				.collect(Collectors.toList());

		return priceFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
	}

	// findPrices with discount
	public static List<String> findPricesWithDiscount(List<Shop> shops, String product) {
		return shops.stream().map(shop -> shop.getProduct() + ":" + shop.getPrice(product) + ":" + Code.GOLD)
				.map(Quote::parse).map(discount -> Code.applyDiscount(discount)).collect(Collectors.toList());
	}

	// findPrices with discount in Asynchronous Mode
	public static List<String> findPricesWithDiscountAsynchronous(List<Shop> shops, String product) {
		// call executor method to schedule effective use of threads in the system
		Executor executor = Shop.executor(shops);

		List<CompletableFuture<String>> priceFutures = shops.stream()
				.map(shop -> CompletableFuture.supplyAsync(
						() -> shop.getProduct() + ":" + shop.getPrice(product) + ":" + Code.GOLD, executor))
				.map(future -> future.thenApply(Quote::parse))
				.map(future -> future
						.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Code.applyDiscount(quote), executor)))
				.collect(Collectors.toList());

		return priceFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());

	}

	// we avoid the creation of a list containing all the prices, and work directly
	// with stream of CompletableFutures, where each completable Future is executing
	// the sequence of operations necessary for a given shop
	public static Stream<CompletableFuture<String>> findPricesStream(List<Shop> shops, String product) {
		// call executor method to schedule effective use of threads in the system
		Executor executor = Shop.executor(shops);

		return shops.stream()
				.map(shop -> CompletableFuture.supplyAsync(
						() -> shop.getProduct() + ":" + shop.getPrice(product) + ":" + Code.GOLD, executor))
				.map(future -> future.thenApply(Quote::parse))
				.map(future -> future.thenCompose(
						quote -> CompletableFuture.supplyAsync(() -> Code.applyDiscount(quote), executor)));
	}

	public double getPrice(String product) {
		return calculatePrice(product);
	}

	private double calculatePrice(String product) {
		Utils.delay();
		return random.nextDouble() * product.charAt(0) + product.charAt(1);
	}

	public static Executor executor(List<Shop> shops) {
		// call executor method to schedule effective use of threads in the system
		Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
			}
		});
		return executor;
	}
}
