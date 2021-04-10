package com.mafia.mafiabackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Сущность Player")
@Data
@Builder
public class PlayerDtoResponse {
    @Schema(description = "Id игрока")
    private Long id;
    @Schema(description = "Имя игрока")
    private String name;
}
