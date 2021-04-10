package com.mafia.mafiabackend.controller;

import com.mafia.mafiabackend.dto.GameInfoDtoRequest;
import com.mafia.mafiabackend.model.GameInfo;
import com.mafia.mafiabackend.service.GameInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GameInfoController {
    private final GameInfoService gameInfoService;

    @PostMapping("/gameInfo")
    public GameInfo changeFouls(@RequestBody GameInfoDtoRequest gameInfoDtoRequest) {
        return gameInfoService.updateGameInfo(gameInfoDtoRequest);
    }
}
