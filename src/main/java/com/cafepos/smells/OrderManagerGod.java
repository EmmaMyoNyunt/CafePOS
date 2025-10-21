package com.cafepos.smells;

import com.cafepos.catalog.Product;
import com.cafepos.catalog.SimpleProduct;
import com.cafepos.decorator.ExtraShot;
import com.cafepos.decorator.OatMilk;
import com.cafepos.common.Money;

import java.math.BigDecimal;

public class OrderManagerGod {

    // 👃 Global State:
    // These static variables store configuration and last-used data across all calls.
    // This breaks encapsulation and makes testing unpredictable.
    public static int TAX_PERCENT = 10;
    public static String LAST_DISCOUNT_CODE = "";

    // 👃 God Class / Long Method:
    // This single method handles product creation, pricing, discounting, tax calculation,
    // payment processing, and receipt printing — violating Single Responsibility Principle.
    public static String process(String recipe, int qty, String paymentType, String discountCode, boolean debug) {

        // 👃 Primitive Obsession:
        // Uses raw strings (recipe names) to represent product configurations.
        // Should be replaced by enums or factory-generated product types.
        Product product;
        if (recipe.equals("ESP+SHOT+OAT")) {
            // 👃 Duplicated Logic:
            // Manual composition repeated for each recipe; could be refactored into a ProductFactory.
            product = new OatMilk(new ExtraShot(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50))));
        } else if (recipe.equals("LAT+L")) {
            product = new SimpleProduct("P-LAT", "Latte", Money.of(3.90));
        } else if (recipe.equals("ESP+SHOT")) {
            product = new ExtraShot(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));
        } else {
            product = new SimpleProduct("P-UNK", "Unknown", Money.of(1.00));
        }

        // 👃 Feature Envy:
        // Accessing decorator pricing directly instead of letting the Product handle it internally.
        Money price = ((com.cafepos.decorator.Priced) product).price();

        // 👃 Data Clump:
        // subtotal, discount, tax, and total always appear together — they should form a single value object.
        Money subtotal = price.multiply(qty);

        // 👃 Temporary Field / Global State:
        // Static variable LAST_DISCOUNT_CODE creates hidden dependencies between method calls.
        Money discount = Money.zero();
        if (discountCode != null && !discountCode.isEmpty()) {
            LAST_DISCOUNT_CODE = discountCode;

            // 👃 Conditional Complexity:
            // Switch mixes multiple discount calculation rules — should be polymorphic (DiscountPolicy pattern).
            switch (discountCode) {
                case "LOYAL5":
                    discount = subtotal.multiply(0.05);
                    break;
                case "COUPON1":
                    discount = Money.of(1.00);
                    if (discount.asBigDecimal().compareTo(subtotal.asBigDecimal()) > 0) {
                        discount = subtotal;
                    }
                    break;
                default:
                    discount = Money.zero();
            }
        }

        Money discounted = Money.of(subtotal.asBigDecimal().subtract(discount.asBigDecimal()));
        if (discounted.asBigDecimal().signum() < 0) discounted = Money.zero();

        // 👃 Magic Number:
        // TAX_PERCENT = 10 hardcoded; should be configurable or injected as a dependency.
        BigDecimal taxAmount = discounted.asBigDecimal()
                .multiply(BigDecimal.valueOf(TAX_PERCENT))
                .divide(BigDecimal.valueOf(100));
        Money tax = Money.of(taxAmount);

        Money total = discounted.add(tax);

        // 👃 Switch-on-Type (Anti-Polymorphism):
        // Payment logic varies by type and should use the Strategy pattern instead.
        String payResult;
        switch (paymentType.toUpperCase()) {
            case "CASH":
                payResult = "[Cash] Customer paid " + total + " EUR in cash.";
                break;
            case "CARD":
                payResult = "[Card] Customer paid " + total + " EUR with card ****1234.";
                break;
            case "WALLET":
                payResult = "[Wallet] Customer paid " + total + " EUR from wallet balance.";
                break;
            default:
                payResult = "[Unknown] Payment method not supported.";
        }

        // 👃 Output Formatting Responsibility:
        // Business logic is mixed with presentation — should be handled by a separate ReceiptPrinter class.
        StringBuilder receipt = new StringBuilder();
        receipt.append("Order (").append(recipe).append(") x").append(qty).append("\n");
        receipt.append("Subtotal: ").append(subtotal).append("\n");
        if (discount.asBigDecimal().signum() > 0) {
            receipt.append("Discount: -").append(discount).append("\n");
        }
        receipt.append("Tax (").append(TAX_PERCENT).append("%): ").append(tax).append("\n");
        receipt.append("Total: ").append(total).append("\n");
        receipt.append(payResult);

        if (debug) {
            // 👃 Debug Flag Smell:
            // Conditional logging adds branching; better handled by a logger.
            System.out.println("[DEBUG] subtotal=" + subtotal + ", discount=" + discount + ", tax=" + tax + ", total=" + total);
        }

        return receipt.toString();
    }
}
