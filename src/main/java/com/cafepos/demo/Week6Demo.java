package com.cafepos.demo;

import com.cafepos.checkout.CheckoutService;
import com.cafepos.pricing.*;
import com.cafepos.receipt.ReceiptPrinter;
import com.cafepos.catalog.SimpleProduct;
import com.cafepos.common.Money;

public class Week6Demo {
    public static void main(String[] args) {
        // 1️⃣ Inject dependencies
        DiscountPolicy discount = new LoyaltyPercentDiscount(5);
        TaxPolicy tax = new FixedRateTaxPolicy(10);
        PricingService pricing = new PricingService(discount, tax);
        ReceiptPrinter printer = new ReceiptPrinter();

        // 2️⃣ Create orchestrator
        CheckoutService checkout = new CheckoutService(pricing, printer);

        // 3️⃣ Create a sample product
        var latte = new SimpleProduct("P-LAT", "Latte", Money.of(3.90));

        // 4️⃣ Process checkout
        String receipt = checkout.checkout(latte, 2, "CARD");

        // 5️⃣ Output
        System.out.println(receipt);
    }
}
