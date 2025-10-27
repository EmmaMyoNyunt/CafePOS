package com.cafepos.receipt;

import com.cafepos.common.Money;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReceiptPrinterTests {

    @Test
    void prints_receipt_in_expected_format() {
        var printer = new ReceiptPrinter();

        String result = printer.print("Latte", 2,
                Money.of(7.80),  // subtotal
                Money.of(0.80),  // discount
                Money.of(0.70),  // tax
                Money.of(7.70)); // total

        assertTrue(result.contains("Product: Latte x2"));
        assertTrue(result.contains("Subtotal: 7.80 EUR"));
        assertTrue(result.contains("Discount: -0.80 EUR"));
        assertTrue(result.contains("Tax: +0.70 EUR"));
        assertTrue(result.contains("TOTAL: 7.70 EUR"));
    }
}
