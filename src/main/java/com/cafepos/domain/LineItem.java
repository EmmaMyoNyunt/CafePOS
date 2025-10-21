package com.cafepos.domain;

import com.cafepos.catalog.Product;
import com.cafepos.common.Money;
import com.cafepos.decorator.Priced;   // 👈 ADD THIS import
import java.util.Objects;

public final class LineItem {
    private final Product product;
    private final int quantity;

    public LineItem(Product product, int quantity) {
        this.product = Objects.requireNonNull(product, "product required");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        this.quantity = quantity;
    }

    public Product product() {
        return product;
    }

    public int quantity() {
        return quantity;
    }

    //  UPDATED to use Priced if available
    public Money lineTotal() {
        Money unitPrice = (product instanceof Priced p) ? p.price() : product.basePrice();
        return unitPrice.multiply(quantity);
    }
}
