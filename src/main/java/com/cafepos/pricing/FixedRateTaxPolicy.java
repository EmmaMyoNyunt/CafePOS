package com.cafepos.pricing;

import com.cafepos.common.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FixedRateTaxPolicy implements TaxPolicy {

    private final BigDecimal rate; // e.g. 0.10 for 10%

    public FixedRateTaxPolicy(double ratePercent) {
        this.rate = BigDecimal.valueOf(ratePercent)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
    }

    @Override
    public Money taxFor(Money amount) {
        // Use Money's multiply method if available
        // or convert using a public method (asBigDecimal / of)
        Money tax = amount.multiply(rate); // if multiply(BigDecimal) exists
        return tax;
    }
}
