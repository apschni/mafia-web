package com.mafia.mafiabackend.dto;

import com.mafia.mafiabackend.model.GameType;
import lombok.Data;

import java.util.List;

@Data
public class GameDtoRequest {
    private GameType gameType;
    private List<Long> playersIds;
}
