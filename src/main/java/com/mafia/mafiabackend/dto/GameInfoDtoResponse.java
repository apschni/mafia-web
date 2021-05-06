package com.mafia.mafiabackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mafia.mafiabackend.model.GameInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
@Schema(description = "Сущность, возвращаемая ручкой /gameInfo и содержащая Set всех GameInfo по измененной игре")
public class GameInfoDtoResponse {
    @Schema(description = "Set всех GameInfo принадлежащих данной игре")
    private List<GameInfo> gameInfos;

    @Schema(description = "Индикатор, показывающий, закончена ли игра (если count(черных) == count(белых)")
    private Boolean gameFinished;
}
