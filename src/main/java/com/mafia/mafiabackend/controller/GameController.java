package com.mafia.mafiabackend.controller;

import com.mafia.mafiabackend.dto.*;
import com.mafia.mafiabackend.service.GameService;
import com.mafia.mafiabackend.validation.GameExists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public Long createGame(@RequestBody @Valid GameDtoRequest gameDtoRequest) {
        return gameService.createGame(gameDtoRequest);
    }

    @Operation(
            summary = "Помечает игру с данным id как завершенную с данным исходом"
    )
    @PostMapping("game/finish")
    public HttpStatus finishGame(@RequestBody @Valid GameFinishDtoRequest gameFinishDtoRequest) {
        return gameService.finishGame(gameFinishDtoRequest);
    }

    @Operation(
            summary = "Тасует роли заново для игры с данным id и типом"
    )
    @PostMapping("/game/reshuffle")
    public GameInfoDtoResponse reshuffleRoles(@RequestBody @Valid GameReshuffleDtoRequest gameReshuffleDtoRequest) {
        return gameService.reshuffleRoles(gameReshuffleDtoRequest.getId(), gameReshuffleDtoRequest.getGameType());
    }

    @Operation(
            summary = "Получение списка последних десяти неактивных игр со списком их игроков и исходами"
    )
    @GetMapping("/game/nonactive")
    public List<NonActiveGameDtoResponse> getLastTenNonActiveGames() {
        return gameService.getLastTenNonActiveGames();
    }

    @Operation(
            summary = "Получение списка всех активных игр со списком их игроков"
    )
    @GetMapping("/game/active")
    public List<ActiveGamesDtoResponse> getActiveGames() {
        return gameService.getActiveGames();
    }

    @Operation(
            summary = "Начать игру"
    )
    @PostMapping("/game/{id}/start")
    public HttpStatus startGame(@PathVariable("id") @GameExists Long gameId) {
        return gameService.startGame(gameId);
    }

}
