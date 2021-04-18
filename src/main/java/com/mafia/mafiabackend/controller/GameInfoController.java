package com.mafia.mafiabackend.controller;

import com.mafia.mafiabackend.dto.GameInfoDtoRequest;
import com.mafia.mafiabackend.dto.GameInfoDtoResponse;
import com.mafia.mafiabackend.service.GameInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "GameInfo Controller", description = "Управление сущностями GameInfo")
public class GameInfoController {

    private final GameInfoService gameInfoService;

    @Operation(
            summary = "Обновление информации о фолах, очках и смерти игрока в базе"
    )
    @PatchMapping("/gameInfo")
    public GameInfoDtoResponse changeGameInfo(@RequestBody GameInfoDtoRequest gameInfoDtoRequest) {
        return gameInfoService.updateGameInfo(gameInfoDtoRequest);
    }

    @Operation(
            summary = "По id игры получает список всех gameInfos (состояний игроков сейчас)"
    )
    @GetMapping("/gameInfo")
    public GameInfoDtoResponse getGameInfos(@RequestBody Long id){
        return gameInfoService.getGameInfos(id);
    }


}
