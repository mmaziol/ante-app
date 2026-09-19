package com.mmaziol.ante.model;

public record Chip(String color, int value) {
    public static final int MIN_CHIP_VALUE = 5;

    public Chip {
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("color cannot be null or blank");
        }

        if (value < MIN_CHIP_VALUE || value % MIN_CHIP_VALUE != 0) {
            throw new IllegalArgumentException("value cannot be less than " + MIN_CHIP_VALUE + " and divisible by " + MIN_CHIP_VALUE);
        }
    }
}