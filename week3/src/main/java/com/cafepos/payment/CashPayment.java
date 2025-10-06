package com.cafepos.payment;

import com.cafepos.domain.Order;

public final class CashPayment implements PaymentStrategy {

    @Override
    public void pay(Order order) {
        // 10% tax is applied using Order's totalWithTax method
        System.out.println("[Cash] Customer paid " + order.totalWithTax(10) + " EUR");
    }
}
