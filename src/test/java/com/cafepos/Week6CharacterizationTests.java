// File: src/test/java/com/cafepos/Week6CharacterizationTests.java
package com.cafepos;

import com.cafepos.smells.OrderManagerGod;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Week-6 Characterization Tests
 *
 * These tests "lock in" the current behavior of OrderManagerGod
 * before refactoring. DO NOT change assertions after refactor;
 * the refactored system must produce the same visible output.
 */
public class Week6CharacterizationTests {

    @Test
    void no_discount_cash_payment() {
        // Arrange & Act
        String receipt = OrderManagerGod.process("ESP+SHOT+OAT", 1, "CASH", "NONE", false);

        // Assert
        assertNotNull(receipt, "Receipt should not be null for cash payment");
        assertAll("cash payment receipt",
                () -> assertTrue(receipt.contains("Espresso"), "Order line missing or incorrect"),
                () -> assertTrue(receipt.contains("Subtotal"), "Subtotal missing"),
                () -> assertTrue(receipt.contains("Discount"), "Discount missing"),
                () -> assertTrue(receipt.contains("Tax"), "Tax line missing"),
                () -> assertTrue(receipt.contains("TOTAL"), "Total line missing"),
                () -> assertTrue(receipt.contains("[Cash]"), "Payment method tag [Cash] missing")
        );
    }

    @Test
    void loyalty_discount_card_payment() {
        String receipt = OrderManagerGod.process("LAT+L", 2, "CARD", "LOYAL5", false);

        assertNotNull(receipt, "Receipt should not be null for card payment with loyalty discount");
        assertAll("card payment with loyalty discount",
                () -> assertTrue(receipt.contains("Latte"), "Order line missing or incorrect"),
                () -> assertTrue(receipt.contains("Subtotal"), "Subtotal missing"),
                () -> assertTrue(receipt.contains("Discount"), "Discount missing"),
                () -> assertTrue(receipt.contains("Tax"), "Tax line missing"),
                () -> assertTrue(receipt.contains("TOTAL"), "Total line missing"),
                () -> assertTrue(receipt.contains("[Card]"), "Card payment confirmation missing")
        );
    }

    @Test
    void coupon_discount_wallet_payment() {
        // qty = 0 is intentional; OrderManagerGod should clamp to 1 internally
        String receipt = OrderManagerGod.process("ESP+SHOT", 0, "WALLET", "COUPON1", false);

        assertNotNull(receipt, "Receipt should not be null for wallet payment with coupon");
        assertAll("wallet payment with coupon and clamped quantity",
                () -> assertTrue(receipt.contains("Espresso"), "Order line missing or incorrect"),
                () -> assertTrue(receipt.contains("x1"), "Quantity 0 should clamp to 1"),
                () -> assertTrue(receipt.contains("Discount"), "Discount line missing"),
                () -> assertTrue(receipt.contains("Tax"), "Tax missing"),
                () -> assertTrue(receipt.contains("[Wallet]"), "Payment method tag [Wallet] missing")
        );
    }
}
