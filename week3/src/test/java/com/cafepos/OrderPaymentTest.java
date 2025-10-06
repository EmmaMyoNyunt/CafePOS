package com.cafepos;

import com.cafepos.catalog.SimpleProduct;
import com.cafepos.common.Money;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.payment.PaymentStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderPaymentTest {

    @Test
    void pay_shouldDelegateToPaymentStrategy() {
        // Arrange: create an order
        Order order = new Order(1);
        order.addItem(new LineItem(new SimpleProduct("P-TEST", "Test Product", Money.of(5)), 1));

        // Track if our fake strategy was called
        final boolean[] called = {false};
        PaymentStrategy fakeStrategy = o -> called[0] = true;

        // Act: call pay with fake strategy
        order.pay(fakeStrategy);

        // Assert: verify that strategy.pay() was called
        assertTrue(called[0], "Expected PaymentStrategy.pay() to be called");
    }
}
