package com.cafepos.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money implements Comparable<Money> {
    private static final int SCALE = 2;
    private static final RoundingMode RM = RoundingMode.HALF_UP;
    private final BigDecimal amount;

    public static Money of (double value) {
        BigDecimal bd = BigDecimal.valueOf(value).setScale(SCALE, RM);
        if (bd.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("amount must be >= 0");
        return new Money(bd);
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO.setScale(SCALE, RM));
    }

    private Money (BigDecimal a) {
        if (a == null) throw new IllegalArgumentException("amount required");
        this.amount = a.setScale(SCALE, RM);
        if (this.amount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("negative money not allowed");
    }

    public Money add(Money other) {
        Objects.requireNonNull(other, "other required");
        return new Money(this.amount.add(other.amount));
    }

    //multiply, e,g, price x quantity
    public Money multiply(int qty) {
        if (qty < 0) throw new IllegalArgumentException("qty must be >= 0");
        return new Money(this.amount.multiply(BigDecimal.valueOf(qty)));
    }

    //multiply, e.g. % taxes
    public Money multiply(BigDecimal factor) {
        Objects.requireNonNull(factor, "factor required");
        return new Money(this.amount.multiply(factor));
    }

    @Override
    public int compareTo(Money o) {
        return this.amount.compareTo(o.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money m = (Money) o;
        return this.amount.compareTo(m.amount) == 0;
    }

    @Override
    public int hashCode() {
        return amount.stripTrailingZeros().hashCode();
    }

    @Override
    public String toString() {
        return amount.setScale(SCALE, RM).toPlainString();
    }
}