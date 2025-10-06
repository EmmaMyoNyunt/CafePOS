package com.cafepos.domain;

import com.cafepos.catalog.Product;
import com.cafepos.common.Money;
import java.util.Objects;

public final class LineItem {
    private final Product product;
    private final int quantity;

    public LineItem(Product product, int quantity) {
        this.product = Objects.requireNonNull(product, "product required");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        this.quantity = quantity;
    }

    public Product product()
    { return product; }
    public int quantity()
    { return quantity; }

    // uses product.basePrice().multiply(quantity)
    public Money lineTotal() {
        return product.basePrice().multiply(quantity);
    }
}
