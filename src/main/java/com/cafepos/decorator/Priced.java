package com.cafepos.decorator;

import com.cafepos.common.Money;

public interface Priced {
    Money price();

    // Adding this method so all products can return their name
    default String name() {
        return getClass().getSimpleName();
    }
}
