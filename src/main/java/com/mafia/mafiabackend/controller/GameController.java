package com.mafia.mafiabackend.controller;

import com.mafia.mafiabackend.dto.GameDtoRequest;
import com.mafia.mafiabackend.dto.GameFinishDtoRequest;
import com.mafia.mafiabackend.dto.GameReshuffleDtoRequest;
import com.mafia.mafiabackend.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Game Controller",
        description = "Управление играми, создание, завершение, перетасовка ролей и получение списка активных игр")
public class GameController {

    private final GameService gameService;

    @Operation(
            summary = "Создание новой игры",
            description = "Позволяет создать игру и автоматически распределяет роли"
    )
    @PostMapping("/game")
    public ResponseEntity<Long> createGame(@RequestBody @Valid GameDtoRequest gameDtoRequest) {
        return ResponseEntity.ok(gameService.createGame(gameDtoRequest));
    }

    @Operation(
            summary = "Помечает игру с данным id как завершенную с данным исходом"
    )
    @PostMapping("game/finish")
    public Long finishGame(@RequestBody @Valid GameFinishDtoRequest gameFinishDtoRequest) {
        return gameService.finishGame(gameFinishDtoRequest);
    }

    @Operation(
            summary = "Тасует роли заново для игры с данным id и типом"
    )
    @PostMapping("/game/reshuffle")
    public Long reshuffleRoles(@RequestBody @Valid GameReshuffleDtoRequest gameReshuffleDtoRequest) {
        return gameService.reshuffleRoles(gameReshuffleDtoRequest.getId(), gameReshuffleDtoRequest.getGameType());
    }

    @Operation(
            summary = "Получение списка id всех активных игр"
    )
    @GetMapping("/game/active")
    public List<Long> getActiveGames() {
        return gameService.getActiveGames();
    }

}
