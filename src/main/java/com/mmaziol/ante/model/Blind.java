package com.mmaziol.ante.model;

public record Blind(int smallBlindValue, int blindIncreaseTimeInMinutes) {
    public static final int BIG_BLIND_MULT = 2;

    public Blind {
        if (smallBlindValue <= 0) {
            throw new IllegalArgumentException("smallBlindValue cannot be zero or negative");
        }

        if (blindIncreaseTimeInMinutes <= 0) {
            throw new IllegalArgumentException("blindIncreaseTimeInMinutes cannot be zero or negative");
        }
    }

    public int bigBlindValue() {
        return this.smallBlindValue * BIG_BLIND_MULT;
    }
}
