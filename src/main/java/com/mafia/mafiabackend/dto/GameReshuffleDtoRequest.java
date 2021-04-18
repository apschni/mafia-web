package com.mafia.mafiabackend.dto;

import com.mafia.mafiabackend.model.GameType;
import lombok.Data;

@Data
public class GameReshuffleDtoRequest {
    private Long id;
    private GameType gameType;
}
