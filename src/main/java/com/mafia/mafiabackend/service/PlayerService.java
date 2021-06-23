package com.mafia.mafiabackend.service;


import com.mafia.mafiabackend.dto.PlayerDtoResponse;
import com.mafia.mafiabackend.model.MonitoringInfo;
import com.mafia.mafiabackend.model.Player;
import com.mafia.mafiabackend.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final ConversionService conversionService;

    public Long addPlayer(String name) {
        if (playerRepository.existsByName(name)) {
            return null;
        }

        Player player = playerRepository.save(Player.builder()
                .name(name)
                .monitoringInfo(MonitoringInfo.builder()
                        .updatedAt(Instant.now())
                        .createdAt(Instant.now())
                        .build())
                .build());
        log.info("Created player: " + player.toString());
        return player.getId();
    }

    public List<PlayerDtoResponse> getAllPlayersOrderedByTotalGamesPlayed() {
        return playerRepository.findAll().stream()
                .filter(player -> player.getGameInfos().stream()
                        .allMatch(gameInfo -> gameInfo.getGame().getGameFinished()))
                .sorted(Comparator.comparing(x -> ((Player) x).getGameInfos().size()).reversed())
                .map(player -> conversionService.convert(player, PlayerDtoResponse.class))
                .collect(Collectors.toList());
    }

    public void deletePlayerById(Long id) {
        if (playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
            log.info("Deleted player with id: " + id);
        } else {
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
        return conversionService.convert(player, PlayerDtoResponse.class);
    }
}
