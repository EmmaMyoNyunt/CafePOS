package com.cafepos.receipt;

import com.cafepos.common.Money;

/**
 * Handles all receipt formatting and printing logic.
 * This separates presentation from business logic.
 */
public class ReceiptPrinter {

    /**
     * Builds a formatted receipt string.
     *
     * @param productName The name of the product.
     * @param quantity The quantity ordered.
     * @param subtotal The subtotal before discounts.
     * @param discount The discount applied.
     * @param tax The tax applied.
     * @param total The final total after all adjustments.
     * @return A formatted receipt as a String.
     */
    public String print(String productName, int quantity, Money subtotal, Money discount, Money tax, Money total) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== Café POS Receipt ===\n");
        sb.append(String.format("Product: %s x%d\n", productName, quantity));
        sb.append(String.format("Subtotal: %s EUR\n", subtotal));
        sb.append(String.format("Discount: -%s EUR\n", discount));
        sb.append(String.format("Tax: +%s EUR\n", tax));
        sb.append("-------------------------\n");
        sb.append(String.format("TOTAL: %s EUR\n", total));
        sb.append("=========================\n");

        return sb.toString();
    }
}
