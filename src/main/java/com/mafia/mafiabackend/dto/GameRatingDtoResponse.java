package com.mafia.mafiabackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameRatingDtoResponse {
    private String playerName;
    private Long totalWins;
    private Long totalLoses;
    private Double rating;
}
