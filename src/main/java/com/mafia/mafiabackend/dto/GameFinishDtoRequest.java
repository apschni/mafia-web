package com.mafia.mafiabackend.dto;

import com.mafia.mafiabackend.model.GameResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Сущность содержащая Id игры и параметр redWin")
public class GameFinishDtoRequest {
    @Schema(description = "Id игры")
    private Long id;
    @Schema(description = "Параметр на завершение игры: если выиграли красные: 1, " +
            "черные: 0, скип игры и удаление из базы: 2")
    private GameResult result;
}
