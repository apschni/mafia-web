package com.mafia.mafiabackend.controller;

import com.mafia.mafiabackend.dto.GameInfoDtoRequest;
import com.mafia.mafiabackend.service.GameInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name="GameInfo Controller", description="Управление сущностями GameInfo")
public class GameInfoController {

    private final GameInfoService gameInfoService;

    @Operation(
            summary = "Обновление информации о фолах, очках и смерти игрока в базе"
    )
    @PatchMapping("/gameInfo")
    public HttpStatus changeFouls(@RequestBody GameInfoDtoRequest gameInfoDtoRequest) {
        gameInfoService.updateGameInfo(gameInfoDtoRequest);
        return HttpStatus.OK;
    }
}
