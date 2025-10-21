package com.cafepos.pricing;

import com.cafepos.common.Money;

/** Strategy interface for all discount calculations. */
public interface DiscountPolicy {
    Money discountOf(Money subtotal);
}
