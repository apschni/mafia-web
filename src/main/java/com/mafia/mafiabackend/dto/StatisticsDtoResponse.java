package com.mafia.mafiabackend.dto;

import com.mafia.mafiabackend.model.Game;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatisticsDtoResponse {
    private String name;
    private List<Game> games;
    private Integer points;
    private Integer winRate;
    private Integer averageFouls;
    private Integer deathCount;
    private Integer winsByBlack;
    private Integer winsByRed;
    private Integer totalGames;
}
