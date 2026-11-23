package com.mmaziol.ante.model;

import com.mmaziol.ante.dto.GameLiveStats;
import com.mmaziol.ante.dto.GameOverview;
import com.mmaziol.ante.dto.PlayerBuyInCount;
import com.mmaziol.ante.dto.PlayerPayout;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class ActiveGame {
    private Ruleset ruleset;
    private Map<Player, Integer> buyIns;
    private int blindIncreaseTimerInSeconds;
    private List<Player> eliminatedPlayers;
    private Instant startedAt;
    private Duration bankedDuration;
    private boolean paused;

    public ActiveGame(Ruleset ruleset) {
        this.setRuleset(ruleset);
        this.setBuyIns(generateDefaultBuyInsPerPlayer(this.ruleset.getPlayers()));
        this.setBlindIncreaseTimerInSeconds(calculateBlindIncreaseTimerToSeconds());
        this.setEliminatedPlayers(new ArrayList<>());
        this.setStartedAt(Instant.now());
        this.setBankedDuration(Duration.ZERO);
        this.setPaused(false);
    }

    public Ruleset getRuleset() {
        return this.ruleset;
    }

    public void setRuleset(Ruleset ruleset) {
        if (ruleset == null) {
            throw new IllegalArgumentException("ruleset cannot be null");
        }

        this.ruleset = ruleset;
    }

    public Map<Player, Integer> getBuyIns() {
        return this.buyIns;
    }

    public void setBuyIns(Map<Player, Integer> buyIns) {
        this.buyIns = buyIns;
    }

    public int getBlindIncreaseTimerInSeconds() {
        return this.blindIncreaseTimerInSeconds;
    }

    public void setBlindIncreaseTimerInSeconds(int blindIncreaseTimerInSeconds) {
        this.blindIncreaseTimerInSeconds = blindIncreaseTimerInSeconds;
    }

    public List<Player> getEliminatedPlayers() {
        return this.eliminatedPlayers;
    }

    public void setEliminatedPlayers(List<Player> eliminatedPLayers) {
        if (eliminatedPLayers == null) {
            throw new IllegalArgumentException("eliminatedPLayers cannot be null");
        }

        for (Player eliminatedPLayersItem : eliminatedPLayers) {
            if (eliminatedPLayersItem == null) {
                throw new IllegalArgumentException("eliminatedPLayers cannot be null");
            }
        }

        this.eliminatedPlayers = new ArrayList<>(eliminatedPLayers);
    }

    public Instant getStartedAt() {
        return this.startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Duration getBankedDuration() {
        return this.bankedDuration;
    }

    public void setBankedDuration(Duration bankedDuration) {
        this.bankedDuration = bankedDuration;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public List<Player> getActivePlayers() {
        List<Player> activePlayers = new ArrayList<>();

        for (Player player : this.ruleset.getPlayers()) {
            if (!this.getEliminatedPlayers().contains(player)) {
                activePlayers.add(player);
            }
        }

        return activePlayers;
    }

    private int calculateBlindIncreaseTimerToSeconds() {
        return (this.getRuleset().getBlind().blindIncreaseTimeInMinutes() * 60);
    }

    private Map<Player, Integer> generateDefaultBuyInsPerPlayer(List<Player> players) {
        Map<Player, Integer> defaults = new HashMap<>();

        for (Player player : players) {
            defaults.put(player, 1);
        }

        return defaults;
    }

    public void addEliminatedPlayer(Player player) {
        if (!this.ruleset.getPlayers().contains(player)) {
            throw new IllegalArgumentException("player does not exist in this game");
        }

        if (this.eliminatedPlayers.contains(player)) {
            throw new IllegalArgumentException("player has been already eliminated");
        }

        if (this.eliminatedPlayers.size() == this.getRuleset().getPlayers().size() - 1) {
            throw new IllegalArgumentException("last player standing cannot be eliminated");
        }

        this.eliminatedPlayers.add(player);
    }

    public void removeEliminatedPlayer(Player player) {
        if (!this.ruleset.getPlayers().contains(player)) {
            throw new IllegalArgumentException("player does not exist in this game");
        }

        if (!this.eliminatedPlayers.contains(player)) {
            throw new IllegalArgumentException("player has not been eliminated");
        }

        this.eliminatedPlayers.remove(player);
    }

    public void addBuyIn(Player player) {
        if (!this.ruleset.getPlayers().contains(player)) {
            throw new IllegalArgumentException("player does not exist in this game");
        }

        int playerValue = this.getBuyIns().get(player);
        this.getBuyIns().replace(player, playerValue + 1);
    }

    public void rebuyPlayer(Player player) {
        this.removeEliminatedPlayer(player);
        this.addBuyIn(player);
    }

    public int getTotalBuyIns() {
        int totalBuyIns = 0;

        for (Player player : this.ruleset.getPlayers()) {
            int playerValue = this.getBuyIns().get(player);
            totalBuyIns += playerValue;
        }

        return totalBuyIns;
    }

    public Player getPlayerByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }

        for (Player player : this.ruleset.getPlayers()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }

        throw new IllegalArgumentException("player does not exist");
    }

    private int getPlayerBuyIn(Player player) {
        return this.buyIns.get(player);
    }

    public int getTotalPot() {
        return (this.getTotalBuyIns() * this.ruleset.getMoney().buyInValue());
    }

    public int getBlindValueBasedOnLevel(int level) {
        int blindValue = this.ruleset.getBlind().smallBlindValue();
        Ladder ladder = this.ruleset.getLadder();

        for (int currentLevel = 2; currentLevel <= level; currentLevel++) {
            Increase increase;

            if (currentLevel <= ladder.getLevelCount()) {
                increase = ladder.getLevelIncreases().get(currentLevel - 2);
            } else {
                increase = ladder.getOvertimeIncrease();
            }

            double multiplier = (100 + increase.percentage()) / 100.0;
            blindValue = (int) (Math.round((blindValue * multiplier) / (double) Chip.MIN_CHIP_VALUE) * Chip.MIN_CHIP_VALUE);
        }

        return blindValue;
    }

    public void stopTimer() {
        if (this.paused) {
            throw new IllegalStateException("game is already paused");
        }

        this.setPaused(true);
        this.setBankedDuration(Duration.between(this.startedAt, Instant.now()));
    }

    public void resumeTimer() {
        if (!this.paused) {
            throw new IllegalStateException("game is already running");
        }

        this.setPaused(false);
        this.setStartedAt(Instant.now().minus(this.bankedDuration));
        this.setBankedDuration(Duration.ZERO);
    }

    public int getElapsedSeconds() {
        if (this.paused) {
            return (int) this.bankedDuration.getSeconds();
        } else {
           return (int) Duration.between(this.startedAt, Instant.now()).getSeconds();
        }
    }

    public int getCurrentLevel() {
        return ((this.getElapsedSeconds() / this.blindIncreaseTimerInSeconds) + 1);
    }

    public int getSecondsUntilNextBlind() {
        return this.blindIncreaseTimerInSeconds - (this.getElapsedSeconds() % this.blindIncreaseTimerInSeconds);
    }

    private List<String> getNameList(List<Player> players) {
        List<String> stringList = new ArrayList<>();


        for (Player player : players) {
            stringList.add(player.getName());
        }

        return stringList;
    }

    private List<PlayerBuyInCount> generateCurrentPlayerBuyInCount() {
        List<PlayerBuyInCount> playerBuyInCounts = new ArrayList<>();

        for (Player player : this.ruleset.getPlayers()) {
            PlayerBuyInCount playerBuyInCount = new PlayerBuyInCount(player.getName(), this.getPlayerBuyIn(player));
            playerBuyInCounts.add(playerBuyInCount);
        }

        return playerBuyInCounts;
    }

    public GameLiveStats getGameLiveStats() {
        int currentSmallBlind = this.getBlindValueBasedOnLevel(this.getCurrentLevel());
        int currentBigBlind = (currentSmallBlind * Blind.BIG_BLIND_MULT);
        int secondsUntilNextBlind = this.getSecondsUntilNextBlind();
        int totalPot = this.getTotalPot();
        List<String> activePlayers = this.getNameList(this.getActivePlayers());
        List<String> eliminatedPlayers = this.getNameList(this.getEliminatedPlayers());
        List<PlayerBuyInCount> buyInCounts = this.generateCurrentPlayerBuyInCount();
        boolean paused = this.paused;

        return new GameLiveStats(currentSmallBlind, currentBigBlind, secondsUntilNextBlind,
                totalPot, activePlayers, eliminatedPlayers, buyInCounts, paused);
    }

    private String getWinner() {
        if (this.getEliminatedPlayers().size() == this.getRuleset().getPlayers().size() - 1) {
            for (Player player : this.getRuleset().getPlayers()) {
                if (!this.getEliminatedPlayers().contains(player)) {
                    return player.getName();
                }
            }
        }

        throw new IllegalStateException("game does not have a winner yet");
    }

    private List<PlayerPayout> generateFinalStandings() {
        int totalPot = this.getTotalPot();
        int totalPlayers = this.getRuleset().getPlayers().size();
        List<PlayerPayout> playerPayouts = new ArrayList<>();

        for (Payout payout : this.getRuleset().getPayouts()) {
            if (payout.place() == 1) {
                PlayerPayout playerPayout = new PlayerPayout(this.getWinner(), payout.place(), (payout.percentageValue() * totalPot) / 100);
                playerPayouts.add(playerPayout);
            } else {
                Player player = this.eliminatedPlayers.get(totalPlayers - payout.place());
                PlayerPayout playerPayout = new PlayerPayout(player.getName(), payout.place(), (payout.percentageValue() * totalPot) / 100);
                playerPayouts.add(playerPayout);
            }
        }

        return playerPayouts;
    }

    public GameOverview getGameOverview() {
        String winner = this.getWinner();
        List<PlayerPayout> finalStandings = this.generateFinalStandings();
        int totalPot = this.getTotalPot();
        List<PlayerBuyInCount> playerBuyInCounts = this.generateCurrentPlayerBuyInCount();

        return new GameOverview(winner, finalStandings, totalPot, playerBuyInCounts);
    }
}
