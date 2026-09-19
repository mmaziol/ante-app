package com.mmaziol.ante.model;

public record Money(int buyInValue, String currency) {
    public Money {
        if (buyInValue <= 0) {
            throw new IllegalArgumentException("buyInValue cannot be zero or negative");
        }

        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("currency cannot be null or blank");
        }
    }
}
