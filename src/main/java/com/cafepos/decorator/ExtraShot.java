package com.cafepos.decorator;

import com.cafepos.catalog.Product;
import com.cafepos.common.Money;

public final class ExtraShot extends ProductDecorator implements Priced {   // 👈 now implements Priced
    private static final Money SURCHARGE = Money.of(0.80);

    public ExtraShot(Product base) {
        super(base);
    }

    @Override
    public String name() {
        return base.name() + " + Extra Shot";
    }

    @Override
    public Money price() {
        // Use price() if the base product implements Priced, otherwise fallback to basePrice()
        return (base instanceof Priced p ? p.price() : base.basePrice()).add(SURCHARGE);
    }
}
