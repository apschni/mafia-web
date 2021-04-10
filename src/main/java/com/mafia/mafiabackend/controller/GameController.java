package com.mafia.mafiabackend.controller;

import com.mafia.mafiabackend.dto.GameDtoRequest;
import com.mafia.mafiabackend.model.Game;
import com.mafia.mafiabackend.repository.GameInfoRepository;
import com.mafia.mafiabackend.repository.GameRepository;
import com.mafia.mafiabackend.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameRepository gameRepository;
    private final GameService gameService;


    @PostMapping("/game")
    public Game createGame(@RequestBody GameDtoRequest gameDtoRequest) {
        return gameService.createGame(gameDtoRequest);

    }

    @GetMapping("/game/all")
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @GetMapping("/game/{id}")
    public Game getGameById(@PathVariable("id") UUID id) {
        return gameRepository.findById(id).orElse(null);
    }


}
