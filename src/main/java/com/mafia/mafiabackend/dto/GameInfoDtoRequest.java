package com.mafia.mafiabackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Сущность GameInfo")
@Data
public class GameInfoDtoRequest {
    @Schema(description = "Id игры")
    private Long id;
    @Schema(description = "Id игрока")
    private Long playerId;
    @Schema(description = "Количество фолов")
    private Integer fouls;
    @Schema(description = "Индикатор жизни")
    private Boolean alive;
    @Schema(description = "Количество очков")
    private Integer points;
}
