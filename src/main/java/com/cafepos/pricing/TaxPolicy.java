package com.cafepos.pricing;

import com.cafepos.common.Money;

public interface TaxPolicy {
    Money taxFor(Money subtotal);
}
