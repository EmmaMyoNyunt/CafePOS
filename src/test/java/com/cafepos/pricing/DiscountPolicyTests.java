package com.cafepos.pricing;

import com.cafepos.common.Money;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DiscountPolicyTests {

    @Test
    void loyaltyDiscount_appliesFivePercent() {
        // Arrange
        DiscountPolicy policy = new LoyaltyPercentDiscount(5);
        Money subtotal = Money.of(100.00);

        // Act
        Money discount = policy.discountOf(subtotal);

        // Assert
        System.out.println("Subtotal = $" + subtotal);
        System.out.println("Discount = $" + discount);
        assertEquals(Money.of(5.00), discount, "5% of $100 should be $5.00");
    }

    @Test
    void fixedCoupon_discountCannotExceedSubtotal() {
        DiscountPolicy policy = new FixedCouponDiscount(Money.of(10.00));
        Money subtotal = Money.of(6.00);
        Money discount = policy.discountOf(subtotal);

        System.out.println("Subtotal = $" + subtotal);
        System.out.println("Discount = $" + discount);
        assertEquals(Money.of(6.00), discount, "Discount should be capped at subtotal");
    }

    @Test
    void noDiscount_returnsZero() {
        DiscountPolicy policy = new NoDiscount();
        Money subtotal = Money.of(42.00);
        Money discount = policy.discountOf(subtotal);

        System.out.println("Subtotal = $" + subtotal);
        System.out.println("Discount = $" + discount);
        assertEquals(Money.zero(), discount, "No discount should be $0.00");
    }
}
