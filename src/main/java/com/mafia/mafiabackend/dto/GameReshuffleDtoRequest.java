package com.mafia.mafiabackend.dto;

import com.mafia.mafiabackend.model.GameType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Schema(description = "Сущность, передаваемая в качестве запроса при перетасовке ролей в данной игре")
public class GameReshuffleDtoRequest {
    @NotNull
    @Schema(description = "Id игры", example = "666")
    private Long id;

    @Schema(description = "Тип игры CLASSIC или KIEV", example = "CLASSIC")
    @NotNull
    private GameType gameType;
}
