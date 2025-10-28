package com.cafepos.domain;

import com.cafepos.common.Money;
import com.cafepos.observer.*;
import com.cafepos.payment.PaymentStrategy;

import java.math.BigDecimal;
import java.util.*;

public final class Order implements OrderPublisher {
    private final long id;
    private final List<LineItem> items = new ArrayList<>();
    private final List<OrderObserver> observers = new ArrayList<>();

    public Order(long id) { this.id = id; }
    public long id() { return id; }

    // ---- Observer management ----
    @Override
    public void register(OrderObserver o) {
        Objects.requireNonNull(o, "observer required");
        if (!observers.contains(o)) observers.add(o);
    }

    @Override
    public void unregister(OrderObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Order order, String eventType) {
        for (OrderObserver o : observers) {
            o.updated(order, eventType);
        }
    }

    // Helper to trigger notifications
    private void notifyObservers(String eventType) {
        notifyObservers(this, eventType);
    }

    // ---- Order behavior ----
    public void addItem(LineItem li) {
        Objects.requireNonNull(li, "line item required");
        if (li.quantity() <= 0) throw new IllegalArgumentException("quantity must be > 0");
        items.add(li);
        notifyObservers("itemAdded");
    }

    public List<LineItem> items() { return Collections.unmodifiableList(items); }

    public Money subtotal() {
        return items.stream()
                .map(LineItem::lineTotal)
                .reduce(Money.zero(), Money::add);
    }

    public Money taxAtPercent(int percent) {
        if (percent < 0) throw new IllegalArgumentException("percent must be >= 0");
        BigDecimal factor = BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100));
        return subtotal().multiply(factor);
    }

    public Money totalWithTax(int percent) {
        return subtotal().add(taxAtPercent(percent));
    }

    // Example of payment event (Week 3 PaymentStrategy assumed)
    public void pay(PaymentStrategy strategy) {
        Objects.requireNonNull(strategy, "strategy required");
        strategy.pay(this);   // pass the whole order object
        notifyObservers("paid");
    }


    public void markReady() {
        notifyObservers("ready");
    }

    public void removeItem(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IndexOutOfBoundsException("Invalid item index.");
        }
        items.remove(index);
        notifyObservers("itemRemoved");
    }
}
