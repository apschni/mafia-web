package com.mafia.mafiabackend.dto;

import com.mafia.mafiabackend.model.Game;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatisticsDtoResponse {
    private String name;
    private List<GameDtoResponse> games;
    private Long points;
    private Long winRate;
    private Double averageFouls;
    private Long deathCount;
    private Long winsByBlack;
    private Long winsByRed;
    private Integer totalGames;
}
