package com.mafia.mafiabackend.service;


import com.mafia.mafiabackend.dto.PlayerDtoResponse;
import com.mafia.mafiabackend.model.Player;
import com.mafia.mafiabackend.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PlayerService {

    private final PlayerRepository playerRepository;

    public Long addPlayer(String name) {
        Player player = Player.builder()
                .name(name).build();
        playerRepository.save(player);
        log.info("Created player: " + player.toString());
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

    public void deletePlayerById(Long id){
        if (playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
            log.info("Deleted player with id: " + id);
        }else {
            log.error("Failed to delete player with id: "
                    + id
                    + String.format(" (no such player in database with id: %d)", id));
        }
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
