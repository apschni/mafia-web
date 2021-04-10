package com.mafia.mafiabackend.dto;

import com.mafia.mafiabackend.model.GameType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "Сущность Game")
@Data
public class GameDtoRequest {
    @Schema(description = "Тип игры CLASSIC/KIEV")
    private GameType gameType;
    @Schema(description = "Id всех игроков")
    private List<Long> playersIds;
}
