package com.mafia.mafiabackend.controller;

import com.mafia.mafiabackend.dto.PlayerDtoRequest;
import com.mafia.mafiabackend.dto.PlayerDtoResponse;
import com.mafia.mafiabackend.dto.StatisticsDtoResponse;
import com.mafia.mafiabackend.service.PlayerService;
import com.mafia.mafiabackend.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "Player Controller",
        description = "Управление игроками, добавление," +
                " получение списка всех игроков, получение игрока по его id, получение статистики игрока")
public class PlayerController {

    private final PlayerService playerService;
    private final StatisticsService statisticsService;

    @Operation(
            summary = "Добавление нового игрока"
    )
    @PostMapping("/player")
    public Long addPlayer(@RequestBody @Valid PlayerDtoRequest playerDtoRequest) {
        return playerService.addPlayer(playerDtoRequest.getName());
    }


    @Operation(
            summary = "Получение списка всех игроков"
    )
    @GetMapping("/player")
    public List<PlayerDtoResponse> getAllPlayers() {
        return playerService.getAllPlayers();
    }


    @Operation(
            summary = "Получение игрока по его id"
    )
    @GetMapping("/player/{id}")
    public PlayerDtoResponse getPlayerById(@PathVariable("id") @NotNull Long id) {
        return playerService.getPlayerById(id);
    }

    @Operation(
            summary = "Удаляет игрока по его id"
    )
    @DeleteMapping("/player/{id}")
    public void deletePlayerById(@PathVariable("id") @NotNull Long id){
        playerService.deletePlayerById(id);
    }

    @Operation(
            summary = "Получает статистику по всем завершённым играм игрока с данным id"
    )
    @GetMapping("/player/{id}/stats")
    public StatisticsDtoResponse getStatisticsByPlayerId(@PathVariable("id") @NotNull Long id){
        return statisticsService.getStatisticsByPlayerId(id);
    }
}
