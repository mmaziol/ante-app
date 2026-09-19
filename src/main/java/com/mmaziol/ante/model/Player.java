package com.mmaziol.ante.model;

public class Player {
    private String name;

    public Player(String name) {
        this.setName(name);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be null or blank");
        }

        this.name = name;
    }
}
