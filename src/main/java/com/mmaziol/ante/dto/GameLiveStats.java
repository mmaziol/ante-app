package com.mmaziol.ante.dto;

import java.util.List;

public record GameLiveStats(int currentSmallBlind, int currentBigBlind, int secondsUntilNextBlind, int totalPot,
                            List<String> activePlayers, List<String> eliminatedPlayers, List<PlayerBuyInCount> buyInCounts, boolean paused) {
    public GameLiveStats {

    }
}
