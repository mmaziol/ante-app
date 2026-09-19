package com.mmaziol.ante.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Ruleset {
    private List<Player> players;
    private List<Chip> chips;
    private List<Payout> payouts;
    private Blind blind;
    private Ladder ladder;
    private Money money;
    private int chipsValuePerPlayer;

    public Ruleset(List<Player> players, List<Chip> chips, List<Payout> payouts, Blind blind, Ladder ladder, Money money, int chipsValuePerPlayer) {
        this.setPlayers(players);
        this.setChips(chips);
        this.setPayouts(payouts);
        this.setBlind(blind);
        this.setLadder(ladder);
        this.setMoney(money);
        this.setChipsValuePerPlayer(chipsValuePerPlayer);
        this.validatePayouts();
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    private void setPlayers(List<Player> players) {
        if (players == null || players.isEmpty() || players.size() < 2) {
            throw new IllegalArgumentException("players cannot be null, empty or be less than 2");
        }

        for (Player playersItem : players) {
            if (playersItem == null) {
                throw new IllegalArgumentException("players cannot be null");
            }
        }

        this.players = new ArrayList<>(players);
    }

    public List<Chip> getChips() {
        return this.chips;
    }

    private void setChips(List<Chip> chips) {
        if (chips == null || chips.isEmpty()) {
            throw new IllegalArgumentException("chips cannot be null or empty");
        }

        for (Chip chipsItem : chips) {
            if (chipsItem == null) {
                throw new IllegalArgumentException("chips cannot be null");
            }
        }

        this.chips = new ArrayList<>(chips);
    }

    public List<Payout> getPayouts() {
        return this.payouts;
    }

    private void setPayouts(List<Payout> payouts) {
        if (payouts == null || payouts.isEmpty()) {
            throw new IllegalArgumentException("payouts cannot be null or empty");
        }

        for (Payout payoutsItem : payouts) {
            if (payoutsItem == null) {
                throw new IllegalArgumentException("payouts cannot be null");
            }
        }

        this.payouts = new ArrayList<>(payouts);
    }

    public Blind getBlind() {
        return this.blind;
    }

    private void setBlind(Blind blind) {
        if (blind == null) {
            throw new IllegalArgumentException("blind cannot be null");
        }

        this.blind = blind;
    }

    public Ladder getLadder() {
        return this.ladder;
    }

    private void setLadder(Ladder ladder) {
        if (ladder == null) {
            throw new IllegalArgumentException("ladder cannot be null");
        }
        this.ladder = ladder;
    }

    public Money getMoney() {
        return this.money;
    }

    private void setMoney(Money money) {
        if (money == null) {
            throw new IllegalArgumentException("money cannot be null");
        }

        this.money = money;
    }

    public int getChipsValuePerPlayer() {
        return this.chipsValuePerPlayer;
    }

    private void setChipsValuePerPlayer(int chipsValuePerPlayer) {
        if (chipsValuePerPlayer <= 0) {
            throw new IllegalArgumentException("chipsValuePerPlayer cannot be zero or negative");
        }

        this.chipsValuePerPlayer = chipsValuePerPlayer;
    }

    public int getTotalPercentageValue() {
        int totalPercentageValue = 0;

        for (Payout payout : this.payouts) {
            totalPercentageValue += payout.percentageValue();
        }

        return totalPercentageValue;
    }

    public boolean isPlaceCorrect() {
        List<Payout> sortedPayouts = new ArrayList<>(this.payouts);
        sortedPayouts.sort(Comparator.comparingInt(Payout::place));

        int expectedPlace = 1;

        for (Payout payout : sortedPayouts) {
            if (payout.place() != expectedPlace) {
                return false;
            }
            expectedPlace++;
        }

        return true;
    }

    public void validatePayouts() {
        if (!(getTotalPercentageValue() == 100)) {
            throw new IllegalArgumentException("totalPercentageValue must equal 100 in total");
        }

        if (!isPlaceCorrect()) {
            throw new IllegalArgumentException("places are not correctly structured");
        }
    }
}
