package BestPriceFinder.bestpricefindershop;

import java.util.Arrays;
import java.util.List;

public class ShopExe2 {

	public static void main(String[] args) {

		List<Shop> shops = Arrays.asList(new Shop("BestPrice"), new Shop("LetsSaveBig"), new Shop("MyFavoriteShop"),
				new Shop("BuyItAll"));

		long start = System.nanoTime();
		System.out.println(Shop.findPricesFuture(shops, "myPhone27s"));
		long duration = (System.nanoTime() - start) / 1_000_000;
		System.out.println("Done in " + duration + " msecs");
	}
}
