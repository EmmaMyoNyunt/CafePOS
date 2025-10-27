package com.cafepos.pricing;

import com.cafepos.common.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Simple fixed-percentage tax policy.
 */
public final class FixedRateTaxPolicy implements TaxPolicy {
    private final int percent; // e.g., 10 for 10%

    public FixedRateTaxPolicy(int percent) {
        if (percent < 0) throw new IllegalArgumentException("percent must be >= 0");
        this.percent = percent;
    }

    @Override
    public Money taxFor(Money amount) {
        BigDecimal t = amount.asBigDecimal()
                .multiply(BigDecimal.valueOf(percent))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return Money.of(t);
    }

    public int percent() { return percent; }
}

//This computes tax using Money instead of doing math in OrderManagerGod//
