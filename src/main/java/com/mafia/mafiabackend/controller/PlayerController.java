package com.mafia.mafiabackend.controller;

import com.mafia.mafiabackend.dto.PlayerDtoResponse;
import com.mafia.mafiabackend.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "Player Controller", description = "Управление сущностями Player")
public class PlayerController {

    private final PlayerService playerService;

    @Operation(
            summary = "Добавление нового игрока"
    )
    @PostMapping("/player")
    public Long addPlayer(@RequestBody String name) {
        return playerService.addPlayer(name);
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
    public PlayerDtoResponse getPlayerById(@PathVariable("id") Long id) {
        return playerService.getPlayerById(id);
    }
}
