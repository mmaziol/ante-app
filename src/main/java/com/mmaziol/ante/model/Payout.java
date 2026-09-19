package com.mmaziol.ante.model;

public record Payout(int place, int percentageValue) {
    public Payout {
        if (place < 1) {
            throw new IllegalArgumentException("place cannot be lower than 1");
        }

        if (percentageValue <= 0) {
            throw new IllegalArgumentException("percentageValue cannot be zero or negative");
        }
    }
}
