package com.mafia.mafiabackend.controller;

import com.mafia.mafiabackend.dto.GameInfoDtoRequest;
import com.mafia.mafiabackend.dto.GameInfoDtoResponse;
import com.mafia.mafiabackend.service.GameInfoService;
import com.mafia.mafiabackend.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@Tag(name = "GameInfo Controller", description = "Управление записями о состоянии игроков в игре," +
        " получение всех состояний игроков для данной игры," +
        " обновление информации о состоянии игроков")
public class GameInfoController {

    private final GameInfoService gameInfoService;
    private final GameService gameService;

    @Operation(
            summary = "Обновление информации о фолах, очках и смерти игрока в базе"
    )
    @PatchMapping("/gameInfo")
    public HttpStatus changeGameInfo(@RequestBody @Valid GameInfoDtoRequest gameInfoDtoRequest) {
        return gameInfoService.updateGameInfo(gameInfoDtoRequest);
    }

    @Operation(
            summary = "По id игры получает список всех gameInfos (состояний игроков сейчас)"
    )
    @GetMapping("/gameInfo/{id}")
    public GameInfoDtoResponse getGameInfos(@PathVariable("id") @NotNull Long id) {
        return gameService.getGameInfos(id);
    }


}
