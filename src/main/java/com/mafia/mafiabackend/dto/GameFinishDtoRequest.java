package com.mafia.mafiabackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Сущность содержащая Id игры и параметр redWin")
public class GameFinishDtoRequest {
    @Schema(description = "Id игры")
    private Long id;
    @Schema(description = "Boolean параметр redWin")
    private Boolean redWin;
}
