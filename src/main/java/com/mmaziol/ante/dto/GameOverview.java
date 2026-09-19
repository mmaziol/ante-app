package com.mmaziol.ante.dto;

import java.util.List;

public record GameOverview(String winner, List<PlayerPayout> finalStandings, int totalPot, List<PlayerBuyInCount> buyInCounts) {

}
