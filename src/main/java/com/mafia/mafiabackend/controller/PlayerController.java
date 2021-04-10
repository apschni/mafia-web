package com.mafia.mafiabackend.controller;

import com.mafia.mafiabackend.model.Player;
import com.mafia.mafiabackend.repository.PlayerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerController {

    private final PlayerRepository playerRepository;

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @PostMapping("/player")
    public Player addPlayer(@RequestParam("name") String name) {
        return playerRepository.save(Player.builder()
                .name(name).build());
    }

    @GetMapping("/player/all")
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @GetMapping("/player/{id}")
    public Player getPlayerById(@PathVariable("id") Long id) {
        return playerRepository.findById(id).orElse(null);
    }

}
