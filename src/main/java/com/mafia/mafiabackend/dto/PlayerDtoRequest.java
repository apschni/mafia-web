package com.mafia.mafiabackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Schema(description = "Сущность, передаваемая в качестве запроса при создании")
public class PlayerDtoRequest {
    @Schema(description = "Имя игрока", example = "Вовчик")
    @NotNull
    @Size(min = 3, max = 10)
    private String name;
}
