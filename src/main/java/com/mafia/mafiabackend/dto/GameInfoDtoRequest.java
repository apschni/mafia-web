package com.mafia.mafiabackend.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class GameInfoDtoRequest {
    private UUID id;
    private Long playerId;
    private Integer fouls;
    private Boolean alive;
}
