package com.cafepos.pricing;

import com.cafepos.common.Money;

/**
 * Strategy interface for tax calculation.
 */
public interface TaxPolicy {
    /**
     * Compute tax for the given taxable amount.
     */
    Money taxFor(Money amount);
}
