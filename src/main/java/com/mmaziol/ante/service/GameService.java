package com.mmaziol.ante.service;

import com.mmaziol.ante.dto.GameOverview;
import com.mmaziol.ante.model.ActiveGame;
import com.mmaziol.ante.dto.GameLiveStats;
import com.mmaziol.ante.model.Player;
import com.mmaziol.ante.model.Ruleset;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private ActiveGame activeGame;
    private GameOverview result;

    public GameService() {
        this.setActiveGame(null);
        this.setResult(null);
    }

    public ActiveGame getActiveGame() {
        return this.activeGame;
    }

    public void setActiveGame(ActiveGame activeGame) {
        this.activeGame = activeGame;
    }

    public GameOverview getResult() {
        return this.result;
    }

    public void setResult(GameOverview result) {
        this.result = result;
    }

    public GameOverview getFinalResult() {
        if (this.result == null) {
            throw new IllegalStateException("no game has finished since the server started");
        }

        return this.result;
    }

    public void startGame(Ruleset ruleset) {
        if (ruleset == null) {
            throw new IllegalArgumentException("ruleset does not exist");
        }

        if (this.activeGame == null && ruleset.isReadyToStart()) {
            ActiveGame activeGame = new ActiveGame(ruleset);
            this.setActiveGame(activeGame);
        } else if (this.activeGame != null) {
            throw new IllegalStateException("game is already running");
        } else if (!ruleset.isReadyToStart()) {
            throw new IllegalStateException("not enough players to start");
        }
    }

    public void finishGame() {
        if (this.activeGame == null) {
            throw new IllegalStateException("game does not exist");
        }

        this.setResult(this.activeGame.getGameOverview());
        this.setActiveGame(null);
    }

    public Player getPlayerByName(String name) {
        if (this.activeGame == null) {
            throw new IllegalStateException("game does not exist");
        }

        return this.activeGame.getPlayerByName(name);
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

    public void rebuyPlayer(Player player) {
        if (this.activeGame == null) {
            throw new IllegalStateException("game does not exist");
        }

        this.activeGame.rebuyPlayer(player);
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
        if (this.activeGame == null) {
            throw new IllegalStateException("game does not exist");
        }

        return this.activeGame.getGameOverview();
    }

    public Ruleset getRuleset() {
        if (this.activeGame == null) {
            throw new IllegalStateException("game does not exist");
        }

        return this.activeGame.getRuleset();
    }
}
