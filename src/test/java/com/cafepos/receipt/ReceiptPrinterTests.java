package com.cafepos.receipt;

import com.cafepos.common.Money;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReceiptPrinterTests {

    @Test
    void prints_expected_receipt_format() {
        // Arrange
        ReceiptPrinter printer = new ReceiptPrinter();

        // Act
        String result = printer.print(
                "Latte",    // product
                1,          // quantity
                Money.of(10.00),  // subtotal
                Money.of(1.00),   // discount
                Money.of(0.90),   // tax
                Money.of(9.90)    // total
        );

        // Print to console (for visual check)
        System.out.println(result);

        // Assert key lines
        assertTrue(result.contains("=== Café POS Receipt ==="));
        assertTrue(result.contains("Product: Latte x1"));
        assertTrue(result.contains("Subtotal: 10.00 EUR"));
        assertTrue(result.contains("Discount: -1.00 EUR"));
        assertTrue(result.contains("Tax: +0.90 EUR"));
        assertTrue(result.contains("TOTAL: 9.90 EUR"));
        assertTrue(result.contains("========================="));
    }
}
