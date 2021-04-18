package com.mafia.mafiabackend.service;


import com.mafia.mafiabackend.dto.PlayerDtoResponse;
import com.mafia.mafiabackend.model.Player;
import com.mafia.mafiabackend.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public Long addPlayer(String name) {
        Player player = Player.builder()
                .name(name).build();
        playerRepository.save(player);
        return player.getId();
    }

    public List<PlayerDtoResponse> getAllPlayers() {
        return playerRepository.findAll().stream()
                .map(player -> PlayerDtoResponse.builder()
                        .id(player.getId())
                        .name(player.getName())
                        .build())
                .collect(Collectors.toList());

    }

    public PlayerDtoResponse getPlayerById(Long id) {
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
