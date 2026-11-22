package com.mmaziol.ante.controller;

import com.mmaziol.ante.dto.GameLiveStats;
import com.mmaziol.ante.dto.GameOverview;
import com.mmaziol.ante.model.Player;
import com.mmaziol.ante.model.Ruleset;
import com.mmaziol.ante.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private GameService gameService;

    public GameController(GameService gameService) {
        this.setGameService(gameService);
    }

    public GameService getGameService() {
        return this.gameService;
    }

    public void setGameService(GameService gameService) {
        if (gameService == null) {
            throw new IllegalArgumentException("gameService cannot be null");
        }

        this.gameService = gameService;
    }

    @GetMapping("/live-stats")
    public GameLiveStats getGameLiveStats() {
        return this.gameService.getGameLiveStats();
    }

    @GetMapping("/ruleset")
    public Ruleset getRuleset() {
        return this.gameService.getRuleset();
    }

    @GetMapping("/overview")
    public GameOverview getGameOverview() {
        return this.gameService.getGameOverview();
    }

    @PostMapping("/start")
    public void startGame(@RequestBody Ruleset request) {
        this.gameService.startGame(request);
    }

    @PostMapping("/finish")
    public void finishGame() {
        this.gameService.finishGame();
    }

    @PostMapping("/buy-in/{playerName}")
    public void buyInPlayer(@PathVariable String playerName) {
        Player player = this.gameService.getPlayerByName(playerName);
        this.gameService.buyInPlayer(player);
    }

    @PostMapping("/eliminate/{playerName}")
    public void addEliminatedPlayer(@PathVariable String playerName) {
        Player player = this.gameService.getPlayerByName(playerName);
        this.gameService.addEliminatedPlayer(player);
    }

    @PostMapping("/remove-from-eliminated/{playerName}")
    public void removeEliminatedPlayer(@PathVariable String playerName) {
        Player player = this.gameService.getPlayerByName(playerName);
        this.gameService.removeEliminatedPlayer(player);
    }

    @PostMapping("/rebuy/{playerName}")
    public void rebuyPlayer(@PathVariable String playerName) {
        Player player = this.gameService.getPlayerByName(playerName);
        this.gameService.rebuyPlayer(player);
    }

    @PostMapping("/timer/stop")
    public void stopTimer() {
        this.gameService.stopTimer();
    }

    @PostMapping("/timer/resume")
    public void resumeTimer() {
        this.gameService.resumeTimer();
    }
}
