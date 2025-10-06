package com.cafepos.payment;

import com.cafepos.domain.Order;
import java.util.Objects;

public final class CardPayment implements PaymentStrategy {
    private final String cardNumber;

    public CardPayment(String cardNumber) {
        this.cardNumber = Objects.requireNonNull(cardNumber, "card number required");
    }

    @Override
    public void pay(Order order) {
        // Mask all but the last 4 digits of the card
        String masked = cardNumber.replaceAll("\\d(?=\\d{4})", "*");
        System.out.println("[Card] Customer paid " + order.totalWithTax(10) +
                " EUR with card " + masked);
    }
}
