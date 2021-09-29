package com.mafia.mafiabackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonWinsDtoResponse {
    private Integer winsByRed;
    private Integer winsByBlack;
    private Integer totalGames;
}
