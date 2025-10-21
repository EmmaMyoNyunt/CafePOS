package com.cafepos.catalog;

import com.cafepos.common.Money;
import com.cafepos.decorator.Priced;   //  import Priced
import java.util.Objects;

public final class SimpleProduct implements Product, Priced {   //  now implements Priced
    private final String id;
    private final String name;
    private final Money basePrice;

    public SimpleProduct(String id, String name, Money basePrice) {
        this.id = Objects.requireNonNull(id, "id required");
        this.name = Objects.requireNonNull(name, "name required");
        this.basePrice = Objects.requireNonNull(basePrice, "basePrice required");
        if (basePrice.compareTo(Money.zero()) < 0)
            throw new IllegalArgumentException("basePrice must be >= 0");
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Money basePrice() {
        return basePrice;
    }

    // 🆕 Step 4: Implement Priced interface
    @Override
    public Money price() {
        // for a simple product, price is just the base price
        return basePrice();
    }
}
