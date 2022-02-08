package BestPriceFinder.bestpricefindershop;

public class Discount {

	public enum Code {
		NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

		private final int percentage;

		Code(int percentage) {
			this.percentage = percentage;
		}

		public static String applyDiscount(Quote quote) {
			return quote.getShopName() + " price is " + apply(quote.getPrice(), quote.getDiscountCode());
		}

		private static double apply(double price, Code code) {
			//Utils.delay();
			//using randomizedDelay
			Utils.randomDelay();
			return Utils.format(price * (100 - code.percentage) / 100);
		}

	}
}
