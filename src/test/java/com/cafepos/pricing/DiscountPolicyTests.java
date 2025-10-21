package com.cafepos.pricing;

import com.cafepos.common.Money;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DiscountPolicyTests {

    @Test
    void loyalty_percent_discount_applies_correctly() {
        DiscountPolicy d = new LoyaltyPercentDiscount(10);
        Money subtotal = Money.of(100.00);
        assertEquals(Money.of(10.00), d.discountOf(subtotal));
    }

    @Test
    void fixed_coupon_never_exceeds_subtotal() {
        DiscountPolicy d = new FixedCouponDiscount(Money.of(5.00));
        assertEquals(Money.of(5.00), d.discountOf(Money.of(10.00)));
        assertEquals(Money.of(3.00), d.discountOf(Money.of(3.00)));
    }

    @Test
    void no_discount_returns_zero() {
        DiscountPolicy d = new NoDiscount();
        assertEquals(Money.zero(), d.discountOf(Money.of(10.00)));
    }
}
