package com.cafepos.pricing;

import com.cafepos.common.Money;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaxPolicyTests {

    @Test
    void fixedRateTax_computesCorrectly() {
        TaxPolicy tax = new FixedRateTaxPolicy(10);
        Money subtotal = Money.of(100.00);
        Money result = tax.taxFor(subtotal);
        System.out.println("Tax = $" + result);
        assertEquals(Money.of(10.00), result);
    }

    @Test
    void zeroPercentTax_returnsZero() {
        TaxPolicy tax = new FixedRateTaxPolicy(0);
        Money subtotal = Money.of(50.00);
        assertEquals(Money.zero(), tax.taxFor(subtotal));
    }
}







