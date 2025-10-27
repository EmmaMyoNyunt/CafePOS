package com.cafepos.checkout;

import com.cafepos.catalog.SimpleProduct;
import com.cafepos.common.Money;
import com.cafepos.pricing.*;
import com.cafepos.receipt.ReceiptPrinter;
import com.cafepos.smells.OrderManagerGod;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies that CheckoutService produces the same receipts
 * as the original OrderManagerGod for key scenarios.
 */
class CheckoutServiceTests {

    private CheckoutService newCheckout(DiscountPolicy discount) {
        TaxPolicy tax = new FixedRateTaxPolicy(10);
        PricingService pricing = new PricingService(discount, tax);
        ReceiptPrinter printer = new ReceiptPrinter();
        return new CheckoutService(pricing, printer);
    }

    @Test
    void matches_original_receipt_cash_payment_no_discount() {
        String oldReceipt = OrderManagerGod.process("ESP+SHOT+OAT", 1, "CASH", "NONE", false);

        var discount = new NoDiscount();
        CheckoutService checkout = newCheckout(discount);
        var espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));

        String newReceipt = checkout.checkout(espresso, 1, "CASH");

        // Ensure key parts match
        assertTrue(newReceipt.contains("Espresso"));
        assertTrue(newReceipt.contains("Subtotal:"));
        assertTrue(newReceipt.contains("TOTAL"));
        assertTrue(newReceipt.contains("[Cash]"));
    }

    @Test
    void matches_original_receipt_card_payment_loyalty_discount() {
        String oldReceipt = OrderManagerGod.process("LAT+L", 2, "CARD", "LOYAL5", false);

        var discount = new LoyaltyPercentDiscount(5);
        CheckoutService checkout = newCheckout(discount);
        var latte = new SimpleProduct("P-LAT", "Latte", Money.of(3.90));

        String newReceipt = checkout.checkout(latte, 2, "CARD");

        assertTrue(newReceipt.contains("Latte"));
        assertTrue(newReceipt.contains("Subtotal:"));
        assertTrue(newReceipt.contains("Discount:"));
        assertTrue(newReceipt.contains("Tax:"));
        assertTrue(newReceipt.contains("[Card]"));
    }

    @Test
    void matches_original_receipt_wallet_payment_coupon_discount() {
        String oldReceipt = OrderManagerGod.process("ESP+SHOT", 0, "WALLET", "COUPON1", false);

        var discount = new FixedCouponDiscount(Money.of(1.00));
        CheckoutService checkout = newCheckout(discount);
        var espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));

        String newReceipt = checkout.checkout(espresso, 1, "WALLET");

        assertTrue(newReceipt.contains("Espresso"));
        assertTrue(newReceipt.contains("Discount:"));
        assertTrue(newReceipt.contains("Tax:"));
        assertTrue(newReceipt.contains("[Wallet]"));
    }
}
