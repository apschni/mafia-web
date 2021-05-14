package com.mafia.mafiabackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Сущность, передаваемая в качестве ответа и содержащая список всех записей по измененной игре" +
        "и индикатор завершения игры")
public class GameInfoDtoResponse {

    @Schema(description = "Список всех состояний, принадлежащих данной игре")
    private List<GameInfoDto> playerInfos;

    @Schema(description = "Индикатор, показывающий, закончена ли игра")
    private Boolean gameFinished;

    private Long gameId;

    private Boolean gameStarted;
}
