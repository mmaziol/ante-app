package com.mmaziol.ante.service;

import com.mmaziol.ante.dto.GameOverview;
import com.mmaziol.ante.model.ActiveGame;
import com.mmaziol.ante.dto.GameLiveStats;
import com.mmaziol.ante.model.Player;
import com.mmaziol.ante.model.Ruleset;

public class GameService {
    private ActiveGame activeGame;

    public GameService() {
        this.setActiveGame(null);
    }

    public ActiveGame getActiveGame() {
        return this.activeGame;
    }

    public void setActiveGame(ActiveGame activeGame) {
        this.activeGame = activeGame;
    }

    public void startGame(Ruleset ruleset) {
        if (this.activeGame == null) {
            ActiveGame activeGame = new ActiveGame(ruleset);
            this.setActiveGame(activeGame);
        } else {
            throw new IllegalStateException("game is already running");
        }
    }

    public void finishGame() {
        this.setActiveGame(null);
    }

    public void buyInPlayer(Player player) {
        if (this.activeGame == null) {
            throw new IllegalStateException("game does not exist");
        }

        this.activeGame.addBuyIn(player);
    }

    public void addEliminatedPlayer(Player player) {
        if (this.activeGame == null) {
            throw new IllegalStateException("game does not exist");
        }

        this.activeGame.addEliminatedPlayer(player);
    }

    public void removeEliminatedPlayer(Player player) {
        if (this.activeGame == null) {
            throw new IllegalStateException("game does not exist");
        }

        this.activeGame.removeEliminatedPlayer(player);
    }

    public void stopTimer() {
        if (this.activeGame == null) {
            throw new IllegalStateException("game does not exist");
        }

        this.activeGame.stopTimer();
    }

    public void resumeTimer() {
        if (this.activeGame == null) {
            throw new IllegalStateException("game does not exist");
        }

        this.activeGame.resumeTimer();
    }

    public GameLiveStats getGameLiveStats() {
        if (this.activeGame == null) {
            throw new IllegalStateException("game does not exist");
        }

        return this.activeGame.getGameLiveStats();
    }

    public GameOverview getGameOverview() {
        return null;
    }
}
