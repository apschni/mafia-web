package com.mafia.mafiabackend.dto;

import com.mafia.mafiabackend.model.GameType;
import com.mafia.mafiabackend.validation.GameExists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Schema(description = "Сущность, передаваемая в качестве запроса при перетасовке ролей в данной игре")
public class GameReshuffleDtoRequest {
    @Schema(description = "Id игры", example = "666")
    @GameExists
    @NotNull
    private Long id;

    @Schema(description = "Тип игры CLASSIC или KIEV", example = "CLASSIC")
    @NotNull
    private GameType gameType;
}
