package com.cafepos;

import com.cafepos.common.Money;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoneyTests {
    @Test void add_and_multiply() {
        assertEquals(Money.of(5.00), Money.of(2.00).add(Money.of(3.00)));
        assertEquals(Money.of(6.00), Money.of(2.00).multiply(3));
    }
}
