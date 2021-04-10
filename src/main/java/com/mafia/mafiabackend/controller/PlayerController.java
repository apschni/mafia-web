package com.mafia.mafiabackend.controller;

import com.mafia.mafiabackend.dto.PlayerDtoResponse;
import com.mafia.mafiabackend.model.Player;
import com.mafia.mafiabackend.repository.PlayerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@Tag(name = "Player Controller", description = "Управление сущностями Player")
public class PlayerController {

    private final PlayerRepository playerRepository;

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Operation(
            summary = "Добавление нового игрока"
    )
    @PostMapping("/player")
    public HttpStatus addPlayer(@RequestBody String name) {
        playerRepository.save(Player.builder()
                .name(name).build());
        return HttpStatus.CREATED;
    }

    @Operation(
            summary = "Получение списка всех игроков"
    )
    @GetMapping("/player")
    public List<PlayerDtoResponse> getAllPlayers() {
        return playerRepository.findAll().stream()
                .map(player -> PlayerDtoResponse.builder()
                        .id(player.getId())
                        .name(player.getName())
                        .build())
                .collect(Collectors.toList());

    }

    @Operation(
            summary = "Получение игрока по его id"
    )
    @GetMapping("/player/{id}")
    public PlayerDtoResponse getPlayerById(@PathVariable("id") Long id) {
        Player player = playerRepository.findById(id).orElse(null);
        if (player == null) {
            return null;
        }
        return PlayerDtoResponse.builder()
                .id(player.getId())
                .name(player.getName())
                .build();
    }

}
