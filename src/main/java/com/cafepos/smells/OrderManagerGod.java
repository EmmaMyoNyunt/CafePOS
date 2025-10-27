package com.cafepos.smells;

import com.cafepos.common.Money;
import com.cafepos.catalog.SimpleProduct;
import com.cafepos.decorator.ExtraShot;
import com.cafepos.decorator.OatMilk;
import com.cafepos.decorator.Priced;
import com.cafepos.pricing.*;
import com.cafepos.receipt.ReceiptPrinter;

/**
 * Final refactored version of OrderManagerGod.
 * Dependencies (discount policy, tax policy, receipt printer) are injected via constructor.
 * No global constants or hardcoded new() calls remain.
 */
public class OrderManagerGod {

    private final DiscountPolicy discountPolicy;
    private final TaxPolicy taxPolicy;
    private final ReceiptPrinter printer;

    //  Constructor Injection
    public OrderManagerGod(DiscountPolicy discountPolicy,
                           TaxPolicy taxPolicy,
                           ReceiptPrinter printer) {
        this.discountPolicy = discountPolicy;
        this.taxPolicy = taxPolicy;
        this.printer = printer;
    }

    /**
     * Processes an order using injected pricing and receipt components.
     */
    public String process(String recipe, int qty, String paymentType, boolean debug) {

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

        //  Clamp qty to at least 1
        int safeQty = Math.max(1, qty);

        //  Calculate subtotal
        Money subtotal = product.price().multiply(safeQty);

        //  Apply pricing via injected policies
        PricingService pricingService = new PricingService(discountPolicy, taxPolicy);
        PricingService.PricingResult result = pricingService.price(subtotal);

        //  Build receipt via injected printer
        String receipt = printer.print(
                product.name(),
                safeQty,
                result.subtotal(),
                result.discount(),
                result.tax(),
                result.total()
        );

        //  Add payment info (still polymorphic later)
        String paymentInfo = switch (paymentType.toUpperCase()) {
            case "CASH" -> "[Cash] Customer paid " + result.total() + " EUR in cash.";
            case "CARD" -> "[Card] Customer paid " + result.total() + " EUR by card.";
            case "WALLET" -> "[Wallet] Customer paid " + result.total() + " EUR from wallet.";
            default -> "[Unknown] Payment method not recognized.";
        };

        return receipt + "\n" + paymentInfo;
    }

    /**
     * Legacy static helper for backward compatibility with older tests.
     * Internally uses constructor injection.
     */
    public static String process(String recipe, int qty, String paymentType,
                                 String discountCode, boolean debug) {

        // Select discount based on code (still behavior-preserving)
        DiscountPolicy discountPolicy = switch (discountCode) {
            case "LOYAL5" -> new LoyaltyPercentDiscount(5);
            case "COUPON1" -> new FixedCouponDiscount(Money.of(1.00));
            default -> new NoDiscount();
        };

        TaxPolicy taxPolicy = new FixedRateTaxPolicy(10);
        ReceiptPrinter printer = new ReceiptPrinter();

        // Injects them (doesn’t create the dependencies — it receives them) //
        OrderManagerGod manager = new OrderManagerGod(discountPolicy, taxPolicy, printer);
        return manager.process(recipe, qty, paymentType, debug);
    }
}
// No static global state.
// All dependencies provided via constructor.
// Behavior preserved (Week6CharacterizationTests should still pass)