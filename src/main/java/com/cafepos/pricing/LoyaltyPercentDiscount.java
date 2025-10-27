package com.cafepos.pricing;

import com.cafepos.common.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class LoyaltyPercentDiscount implements DiscountPolicy {
    private final int percent;

    public LoyaltyPercentDiscount(int percent) {
        if (percent < 0 || percent > 100)
            throw new IllegalArgumentException("percent must be between 0 and 100");
        this.percent = percent;
    }

    @Override
    public Money discountOf(Money subtotal) {
        BigDecimal d = subtotal.asBigDecimal()
                .multiply(BigDecimal.valueOf(percent))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return Money.of(d);
    }
}
