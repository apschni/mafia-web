package com.mafia.mafiabackend.service;

import com.mafia.mafiabackend.dto.GameDtoResponse;
import com.mafia.mafiabackend.dto.GameRatingDtoResponse;
import com.mafia.mafiabackend.dto.StatisticsDtoResponse;
import com.mafia.mafiabackend.model.Game;
import com.mafia.mafiabackend.model.GameInfo;
import com.mafia.mafiabackend.model.Player;
import com.mafia.mafiabackend.model.Role;
import com.mafia.mafiabackend.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
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
        List<Game> games = gameInfos.stream()
                .map(GameInfo::getGame)
                .collect(Collectors.toList());

        List<GameDtoResponse> gameDtoResponses = new ArrayList<>();
        games.forEach(game -> gameDtoResponses.add(GameDtoResponse.builder()
                .gameFinished(game.getGameFinished())
                .gameType(game.getGameType())
                .id(game.getId())
                .playerWins(isPlayerWon(game, player.getId()))
                .build()));

        return StatisticsDtoResponse.builder()
                .name(player.getName())
                .games(gameDtoResponses)
                .points(getPointsCount(gameInfos))
                .winRate(getWinRate(gameInfos))
                .averageFouls(getAverageFouls(gameInfos))
                .deathCount(getDeathCount(gameInfos))
                .winsByRed(getWinsByRole(true, gameInfos))
                .winsByBlack(getWinsByRole(false, gameInfos))
                .totalGames(gameInfos.size())
                .build();
    }


    private Boolean isPlayerWon(Game game, Long playerId) {
        return game.getGameInfos().stream()
                .filter(gameInfo -> gameInfo.getPlayerId().equals(playerId))
                .map(GameInfo::getRole)
                .anyMatch(role -> !Role.isBlack(role) == game.getRedWin());
    }

    private Long getWinsByRole(Boolean isRed, List<GameInfo> gameInfos) {
        return isRed ? gameInfos.stream()
                .filter(gameInfo -> !Role.isBlack(gameInfo.getRole()) && gameInfo.getGame().getRedWin())
                .count() : gameInfos.stream()
                .filter(gameInfo -> Role.isBlack(gameInfo.getRole()) && !gameInfo.getGame().getRedWin())
                .count();

    }

    private Long getWinRate(List<GameInfo> gameInfos) {
        return (getWinsByRole(true, gameInfos) + getWinsByRole(false, gameInfos)) / gameInfos.size();
    }

    private Long getPointsCount(List<GameInfo> gameInfos) {
        return gameInfos.stream()
                .mapToLong(GameInfo::getPoints)
                .sum();
    }

    private Double getAverageFouls(List<GameInfo> gameInfos) {
        OptionalDouble average = gameInfos.stream()
                .mapToInt(GameInfo::getFoul)
                .average();
        return average.isPresent() ? average.getAsDouble() : 0;
    }

    private Long getDeathCount(List<GameInfo> gameInfos) {
        return gameInfos.stream()
                .filter(gameInfo -> !gameInfo.getAlive())
                .count();
    }

    public List<GameRatingDtoResponse> getPlayersRating() {
        return playerRepository.findAll().stream()
                .map(player -> {
                    List<GameInfo> gameInfos = player.getGameInfos().stream()
                            .filter(gameInfo -> gameInfo.getGame().getGameFinished())
                            .collect(Collectors.toList());
                    long totalWins = getWinsByRole(true, gameInfos) + getWinsByRole(false, gameInfos);
                    return GameRatingDtoResponse.builder()
                            .playerName(player.getName())
                            .totalWins(totalWins)
                            .totalGames((long) gameInfos.size())
                            .rating(gameInfos.size() == 0 ? 0 : Math.pow(totalWins, 2) / gameInfos.size())
                            .build();
                })
                .sorted(Comparator.comparing(x -> ((GameRatingDtoResponse) x).getRating()).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}
