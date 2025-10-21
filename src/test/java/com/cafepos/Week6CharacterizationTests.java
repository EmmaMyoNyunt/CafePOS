package com.cafepos;

import com.cafepos.smells.OrderManagerGod;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Week6CharacterizationTests {

    @Test
    void no_discount_cash_payment() {
        String receipt = OrderManagerGod.process("ESP+SHOT+OAT", 1, "CASH", "NONE", false);

        assertTrue(receipt.contains("Order (ESP+SHOT+OAT) x1"));
        assertTrue(receipt.contains("Subtotal:"));
        assertTrue(receipt.contains("Tax (10%):"));
        assertTrue(receipt.contains("Total:"));
        assertTrue(receipt.contains("[Cash]"));
    }

    @Test
    void loyalty_discount_card_payment() {
        String receipt = OrderManagerGod.process("LAT+L", 2, "CARD", "LOYAL5", false);

        assertTrue(receipt.contains("Order (LAT+L) x2"));
        assertTrue(receipt.contains("Discount:"));
        assertTrue(receipt.contains("Tax (10%):"));
        assertTrue(receipt.contains("[Card] Customer paid"));
    }

    @Test
    void coupon_discount_wallet_payment() {
        String receipt = OrderManagerGod.process("ESP+SHOT", 0, "WALLET", "COUPON1", false);

        assertTrue(receipt.contains("x1"), "Quantity 0 should clamp to 1");
        assertTrue(receipt.contains("Discount:"));
        assertTrue(receipt.contains("[Wallet]"));
    }
}
