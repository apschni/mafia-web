package com.mafia.mafiabackend.dto;

import com.mafia.mafiabackend.model.GameType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(hidden = true)
public class GameDtoResponse {

    private Long id;

    private GameType gameType;

    private Boolean playerWins;

    private Boolean gameFinished;

}
