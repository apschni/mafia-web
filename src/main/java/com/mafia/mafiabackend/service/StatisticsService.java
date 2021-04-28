package com.mafia.mafiabackend.service;

import com.mafia.mafiabackend.dto.StatisticsDtoResponse;
import com.mafia.mafiabackend.model.Game;
import com.mafia.mafiabackend.model.GameInfo;
import com.mafia.mafiabackend.model.Player;
import com.mafia.mafiabackend.model.Role;
import com.mafia.mafiabackend.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticsService {
    private final PlayerRepository playerRepository;

    public StatisticsDtoResponse getStatisticsByPlayerId(Long id) {
        Player player = playerRepository.findById(id).orElse(null);

        if (player == null) {
            return null;
        }

        List<GameInfo> gameInfos = player.getGameInfos().stream()
                .filter(gameInfo -> gameInfo.getGame().getGameFinished())
                .collect(Collectors.toList());

        if (gameInfos.isEmpty()) {
            return null;
        }
        List<Game> games = gameInfos.stream().map(GameInfo::getGame).collect(Collectors.toList());

        return StatisticsDtoResponse.builder()
                .name(player.getName())
                .games(games)
                .points(getPointsCount(gameInfos))
                .winRate(getWinRate(gameInfos))
                .averageFouls(getAverageFouls(gameInfos))
                .deathCount(getDeathCount(gameInfos))
                .winsByRed(getWinsBy(true, gameInfos))
                .winsByBlack(getWinsBy(false, gameInfos))
                .totalGames(gameInfos.size())
                .build();
    }

    private Integer getWinsBy(Boolean isRed, List<GameInfo> gameInfos) {
        int counterRed = 0;
        int counterBlack = 0;

        for (GameInfo gameInfo : gameInfos) {
            if (isRed && !Role.isBlack(gameInfo.getRole()) && gameInfo.getGame().getRedWin()) {
                counterRed++;
            }
            if (!isRed && Role.isBlack(gameInfo.getRole()) && !gameInfo.getGame().getRedWin()) {
                counterBlack++;
            }
        }
        return isRed ? counterRed : counterBlack;
    }

    private Integer getWinRate(List<GameInfo> gameInfos) {
        return (getWinsBy(true, gameInfos) + getWinsBy(false, gameInfos)) / gameInfos.size();
    }

    private Integer getPointsCount(List<GameInfo> gameInfos) {
        Integer counter = 0;
        for (GameInfo gameInfo : gameInfos) {
            counter += gameInfo.getPoints();
        }
        return counter;
    }

    private Integer getAverageFouls(List<GameInfo> gameInfos) {
        Integer counter = 0;
        for (GameInfo gameInfo : gameInfos) {
            counter += gameInfo.getFoul();
        }
        return counter / gameInfos.size();
    }

    private Integer getDeathCount(List<GameInfo> gameInfos) {
        Integer counter = 0;
        for (GameInfo gameInfo : gameInfos) {
            if (!gameInfo.getAlive()) {
                counter++;
            }
        }
        return counter;
    }
}
