package com.mmaziol.ante.model;

public enum Increase {
    QUARTER(25),
    THIRD(33),
    HALF(50),
    DOUBLE(100);

    private final int percentage;

    Increase(int percentage) {
        this.percentage = percentage;
    }

    public int percentage() {
        return this.percentage;
    }
}
