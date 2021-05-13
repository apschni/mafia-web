package com.mafia.mafiabackend.dto;

import com.mafia.mafiabackend.validation.GameExists;
import com.mafia.mafiabackend.validation.PlayerExists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Schema(description = "Сущность, передаваемая в качестве запроса при обновлении информации об игроке")
@Data
public class GameInfoDtoRequest {
    @Schema(description = "Id игры", example = "305")
    @GameExists
    @NotNull
    private Long id;

    @Schema(description = "Id игрока", example = "404")
    @PlayerExists
    @NotNull
    private Long playerId;

    @Schema(description = "Количество фолов", example = "2")
    @Min(0)
    @Max(4)
    private Integer fouls;

    @Schema(description = "Индикатор жизни", example = "false")
    private Boolean alive;

    @Schema(description = "Количество очков", example = "3")
    private Integer points;
}
