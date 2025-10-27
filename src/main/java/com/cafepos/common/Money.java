package com.cafepos.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money implements Comparable<Money> {

    private static final int SCALE = 2;
    private static final RoundingMode RM = RoundingMode.HALF_UP;
    private final BigDecimal amount;

    // --- Factory methods ---

    public static Money of(double value) {
        BigDecimal bd = BigDecimal.valueOf(value).setScale(SCALE, RM);
        if (bd.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("amount must be >= 0");
        return new Money(bd);
    }

    public static Money of(BigDecimal value) {
        Objects.requireNonNull(value, "value required");
        BigDecimal scaled = value.setScale(SCALE, RM);
        if (scaled.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("amount must be >= 0");
        return new Money(scaled);
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO.setScale(SCALE, RM));
    }

    // --- Constructor (private to enforce factory use) ---

    private Money(BigDecimal a) {
        if (a == null) throw new IllegalArgumentException("amount required");
        this.amount = a.setScale(SCALE, RM);
        if (this.amount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("negative money not allowed");
    }

    // --- Arithmetic operations ---

    public Money add(Money other) {
        Objects.requireNonNull(other, "other required");
        return new Money(this.amount.add(other.amount).setScale(SCALE, RM));
    }

    /** New: safe subtraction (subtotal - discount) */
    public Money subtract(Money other) {
        Objects.requireNonNull(other, "other required");
        BigDecimal result = this.amount.subtract(other.amount).setScale(SCALE, RM);
        // prevent negative amounts
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            result = BigDecimal.ZERO.setScale(SCALE, RM);
        }
        return new Money(result);
    }

    // multiply by quantity (integer)
    public Money multiply(int qty) {
        if (qty < 0) throw new IllegalArgumentException("qty must be >= 0");
        if (qty == 0) return Money.zero();
        return new Money(this.amount.multiply(BigDecimal.valueOf(qty)).setScale(SCALE, RM));
    }

    // multiply by percentage or arbitrary factor
    public Money multiply(BigDecimal factor) {
        Objects.requireNonNull(factor, "factor required");
        return new Money(this.amount.multiply(factor).setScale(SCALE, RM));
    }

    // multiply by double factor, e.g. 0.05 for 5%
    public Money multiply(double factor) {
        return multiply(BigDecimal.valueOf(factor));
    }

    // --- Accessors / Helpers ---

    public BigDecimal asBigDecimal() {
        return this.amount;
    }

    // --- Comparable & equality ---

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
