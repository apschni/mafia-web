package com.mafia.mafiabackend.dto;

import com.mafia.mafiabackend.model.GameResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
@Schema(description = "Сущность,передаваемая в качестве запроса и содержащая id игры и параметр result")
public class GameFinishDtoRequest {
    @Schema(description = "Id игры", example = "666")
    @NotNull
    private Long id;

    @Schema(description = "Параметр на завершение игры: если выиграли красные: RED_WIN, " +
            "черные: BLACK_WIN, скип игры и удаление из базы: SKIP_AND_DELETE", example = "RED_WIN")
    @NotNull
    private GameResult result;
}
