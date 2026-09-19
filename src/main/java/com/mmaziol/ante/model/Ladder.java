package com.mmaziol.ante.model;

import java.util.ArrayList;
import java.util.List;

public class Ladder {
    private static final int DEFAULT_LEVEL_COUNT = 12;
    private static final int MAX_LEVEL_COUNT = 50;
    private int levelCount;
    private Increase overtimeIncrease;
    private List<Increase> levelIncreases;

    public Ladder() {
        this.setLevelCount(DEFAULT_LEVEL_COUNT);
        this.setOvertimeIncrease(Increase.DOUBLE);
        this.setLevelIncreases(generateDefaultLevelIncreases(this.levelCount));
        this.validateLevelIncreases();
    }

    public int getLevelCount() {
        return this.levelCount;
    }

    public void setLevelCount(int levelCount) {
        if (levelCount <= 1 || levelCount > MAX_LEVEL_COUNT) {
            throw new IllegalArgumentException("levelCount must be greater than 1 and at most " + MAX_LEVEL_COUNT);
        }

        this.levelCount = levelCount;

        if (this.levelIncreases != null) {
            this.resizeLevelIncreases();
        }
    }

    private void resizeLevelIncreases() {
        int requiredSize = this.levelCount - 1;
        List<Increase> resized = new ArrayList<>(this.levelIncreases);

        while (resized.size() > requiredSize) {
            resized.removeLast();
        }

        while (resized.size() < requiredSize) {
            resized.add(Increase.DOUBLE);
        }

        this.levelIncreases = resized;
    }

    public Increase getOvertimeIncrease() {
        return this.overtimeIncrease;
    }

    public void setOvertimeIncrease(Increase overtimeIncrease) {
        this.overtimeIncrease = overtimeIncrease;
    }

    public List<Increase> getLevelIncreases() {
        return this.levelIncreases;
    }

    public void setLevelIncreases(List<Increase> levelIncreases) {
        if (levelIncreases == null || levelIncreases.isEmpty()) {
            throw new IllegalArgumentException("levelIncreases cannot be null or empty");
        }

        for (Increase levelIncreasesItem : levelIncreases) {
            if (levelIncreasesItem == null) {
                throw new IllegalArgumentException("levelIncreases cannot be null");
            }
        }

        this.levelIncreases = new ArrayList<>(levelIncreases);
    }

    private List<Increase> generateDefaultLevelIncreases(int levelCount) {
        int rounds = levelCount - 1;
        List<Increase> defaults = new ArrayList<>();

        for (int i = 0; i < rounds; i++) {
            defaults.add(Increase.DOUBLE);
        }

        return defaults;
    }

    public void setLevelIncrease(int level, Increase increase) {
        level = level - 2;

        if (level < 0 || level >= this.levelIncreases.size()) {
            throw new IllegalArgumentException("level cannot be negative has to be within the levelIncrease size");
        }

        this.levelIncreases.set(level, increase);
    }

    public void validateLevelIncreases() {
        if (this.levelIncreases.size() != this.levelCount - 1) {
            throw new IllegalArgumentException("levelIncreases size must equal levelCount - 1");
        }
    }
}
