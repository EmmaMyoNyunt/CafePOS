package com.cafepos.domain;

import java.util.concurrent.atomic.AtomicLong;

public final class OrderIds {
    private static final AtomicLong NEXT = new AtomicLong(1000);
    public static long next() {
        return NEXT.incrementAndGet(); }
    private OrderIds() {
    }
}
