package com.mafia.mafiabackend.dto;


import com.mafia.mafiabackend.model.GameInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameInfoInnerDto {
    private String playerName;
    private GameInfo gameInfo;
}
