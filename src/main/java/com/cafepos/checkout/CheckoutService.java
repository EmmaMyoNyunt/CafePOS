package com.cafepos.checkout;

import com.cafepos.common.Money;
import com.cafepos.decorator.Priced;
import com.cafepos.pricing.*;
import com.cafepos.receipt.ReceiptPrinter;

/**
 * CheckoutService acts as a clean orchestrator.
 * It coordinates pricing, tax, and receipt printing.
 * It has no business logic itself.
 */
public class CheckoutService {

    private final PricingService pricingService;
    private final ReceiptPrinter printer;

    // 🧩 Constructor Injection
    public CheckoutService(PricingService pricingService, ReceiptPrinter printer) {
        this.pricingService = pricingService;
        this.printer = printer;
    }

    /**
     * Processes a checkout operation.
     * @param product The priced product.
     * @param quantity The quantity ordered.
     * @param paymentType Payment method (e.g. "CASH", "CARD", "WALLET").
     * @return A formatted receipt string.
     */
    public String checkout(Priced product, int quantity, String paymentType) {

        int safeQty = Math.max(1, quantity);
        Money subtotal = product.price().multiply(safeQty);

        // Delegate all pricing logic
        PricingService.PricingResult result = pricingService.price(subtotal);

        // Delegate receipt printing
        String receipt = printer.print(
                product.name(),
                safeQty,
                result.subtotal(),
                result.discount(),
                result.tax(),
                result.total()
        );

        // Add payment info
        String paymentInfo = switch (paymentType.toUpperCase()) {
            case "CASH" -> "[Cash] Customer paid " + result.total() + " EUR in cash.";
            case "CARD" -> "[Card] Customer paid " + result.total() + " EUR by card.";
            case "WALLET" -> "[Wallet] Customer paid " + result.total() + " EUR from wallet.";
            default -> "[Unknown] Payment method not recognized.";
        };

        return receipt + "\n" + paymentInfo;
    }
}
