package com.cafepos.smells;

import com.cafepos.common.Money;
import com.cafepos.catalog.SimpleProduct;
import com.cafepos.decorator.ExtraShot;
import com.cafepos.decorator.OatMilk;
import com.cafepos.decorator.Priced;
import com.cafepos.pricing.*;
import com.cafepos.receipt.ReceiptPrinter;

/**
 * Refactored version of the God class.
 * Delegates pricing, tax, and receipt printing to dedicated components.
 */
public class OrderManagerGod {

    public static String process(String recipe, int qty, String paymentType, String discountCode, boolean debug) {
        // Build product
        Priced product = switch (recipe) {
            case "ESP+SHOT+OAT" ->
                    new OatMilk(new ExtraShot(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50))));
            case "LAT+L" ->
                    new SimpleProduct("P-LAT", "Latte", Money.of(3.90));
            case "ESP+SHOT" ->
                    new ExtraShot(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));
            default ->
                    new SimpleProduct("P-UNK", "Unknown", Money.of(1.00));
        };

        // Clamp qty to at least 1 (fixes "qty must be >= 0" error)
        int safeQty = Math.max(1, qty);

        // Calculate subtotal
        Money subtotal = product.price().multiply(safeQty);

        // Apply discount & tax through PricingService
        DiscountPolicy discountPolicy = switch (discountCode) {
            case "LOYAL5" -> new LoyaltyPercentDiscount(5);
            case "COUPON1" -> new FixedCouponDiscount(Money.of(1.00));
            default -> new NoDiscount();
        };
        TaxPolicy taxPolicy = new FixedRateTaxPolicy(10); // 10% VAT

        PricingService pricingService = new PricingService(discountPolicy, taxPolicy);
        PricingService.PricingResult result = pricingService.price(subtotal);

        // 5️⃣ Print receipt using ReceiptPrinter
        ReceiptPrinter printer = new ReceiptPrinter();
        String receipt = printer.print(
                product.name(),
                safeQty,                // ✅ FIXED — use safeQty here
                result.subtotal(),
                result.discount(),
                result.tax(),
                result.total()
        );

        // 6️⃣ Add payment info
        String paymentInfo = switch (paymentType.toUpperCase()) {
            case "CASH" -> "[Cash] Customer paid " + result.total() + " EUR in cash.";
            case "CARD" -> "[Card] Customer paid " + result.total() + " EUR by card.";
            case "WALLET" -> "[Wallet] Customer paid " + result.total() + " EUR from wallet.";
            default -> "[Unknown] Payment method not recognized.";
        };

        // 7️⃣ Combine both outputs
        return receipt + "\n" + paymentInfo;
    }
}
