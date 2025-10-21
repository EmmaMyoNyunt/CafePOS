package com.cafepos.pricing;

import com.cafepos.common.Money;

/** A fixed-amount discount, e.g. coupon worth 1 EUR. */
public final class FixedCouponDiscount implements DiscountPolicy {
    private final Money amount;

    public FixedCouponDiscount(Money amount) {
        this.amount = amount;
    }

    @Override
    public Money discountOf(Money subtotal) {
        if (amount.asBigDecimal().compareTo(subtotal.asBigDecimal()) > 0) {
            return subtotal; // never more than subtotal
        }
        return amount;
    }
}
