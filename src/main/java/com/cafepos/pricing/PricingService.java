package com.cafepos.pricing;

import com.cafepos.common.Money;

public class PricingService {

    private final DiscountPolicy discountPolicy;
    private final TaxPolicy taxPolicy;

    public PricingService(DiscountPolicy discountPolicy, TaxPolicy taxPolicy) {
        this.discountPolicy = discountPolicy;
        this.taxPolicy = taxPolicy;
    }

    public PricingResult price(Money subtotal) {
        // Step 1: compute discount
        Money discount = discountPolicy.discountOf(subtotal);

        // Step 2: compute taxable amount (subtotal - discount)
        Money taxableAmount = subtotal.subtract(discount);

        // Step 3: compute tax on the discounted amount
        Money tax = taxPolicy.taxFor(taxableAmount);

        // Step 4: compute total = subtotal - discount + tax
        Money total = taxableAmount.add(tax);

        // Step 5: return result record
        return new PricingResult(subtotal, discount, tax, total);
    }

    // --- Nested result record ---
    public static class PricingResult {
        private final Money subtotal;
        private final Money discount;
        private final Money tax;
        private final Money total;

        public PricingResult(Money subtotal, Money discount, Money tax, Money total) {
            this.subtotal = subtotal;
            this.discount = discount;
            this.tax = tax;
            this.total = total;
        }

        public Money subtotal() { return subtotal; }
        public Money discount() { return discount; }
        public Money tax() { return tax; }
        public Money total() { return total; }
    }
}
