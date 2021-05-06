package com.mafia.mafiabackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Сущность, передаваемая в качестве ответа при запросах," +
        " связанных с получением информации об игроке")
@Data
@Builder
public class PlayerDtoResponse {
    @Schema(description = "id игрока", example = "303")
    private Long id;
    @Schema(description = "Имя игрока", example = "Вовчик")
    private String name;
}
