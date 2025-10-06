package com.cafepos.domain;

import com.cafepos.common.Money;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Order {
    private final long id;
    private final List<LineItem> items = new ArrayList<>();

    public Order(long id) { this.id = id; }

    public long id() { return id; }

    public void addItem(LineItem li) {
        Objects.requireNonNull(li, "line item required");
        if (li.quantity() <= 0) throw new IllegalArgumentException("quantity must be > 0");
        items.add(li);
    }

    public List<LineItem> items() { return Collections.unmodifiableList(items); }

    public Money subtotal() {
        return items.stream()
                .map(LineItem::lineTotal)
                .reduce(Money.zero(), Money::add);
    }

    // tax percent as integer (e.g., 10 means 10%)
    public Money taxAtPercent(int percent) {
        if (percent < 0) throw new IllegalArgumentException("percent must be >= 0");
        BigDecimal factor = BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100));
        return subtotal().multiply(factor);
    }

    public Money totalWithTax(int percent) {
        return subtotal().add(taxAtPercent(percent));
    }
}
