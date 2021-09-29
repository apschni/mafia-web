package com.mafia.mafiabackend.service;

import com.mafia.mafiabackend.dto.CommonWinsDtoResponse;
import com.mafia.mafiabackend.dto.GameDtoResponse;
import com.mafia.mafiabackend.dto.GameRatingDtoResponse;
import com.mafia.mafiabackend.dto.StatisticsDtoResponse;
import com.mafia.mafiabackend.model.*;
import com.mafia.mafiabackend.repository.CommonStatisticsRepository;
import com.mafia.mafiabackend.repository.GameInfoRepository;
import com.mafia.mafiabackend.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticsService {
    private final PlayerRepository playerRepository;
    private final GameInfoRepository gameInfoRepository;
    private final CommonStatisticsRepository commonStatisticsRepository;

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
                .winsByRed(getWinsByRoleType(true, gameInfos))
                .winsByBlack(getWinsByRoleType(false, gameInfos))
                .gamesByVillager(getTotalGamesPlayedByRole(Role.RED, gameInfos))
                .gamesByBlackRole(getTotalGamesPlayedByRole(Role.BLACK, gameInfos))
                .gamesByDon(getTotalGamesPlayedByRole(Role.DON, gameInfos))
                .gamesBySheriff(getTotalGamesPlayedByRole(Role.SHERIFF, gameInfos))
                .winsByVillager(getWinsByRole(Role.RED, gameInfos))
                .winsByBlackRole(getWinsByRole(Role.BLACK, gameInfos))
                .winsByDon(getWinsByRole(Role.DON, gameInfos))
                .winsBySheriff(getWinsByRole(Role.SHERIFF, gameInfos))
                .totalGames(gameInfos.size())
                .build();
    }

    public void updateCommonStatisticWithPastGames(){
        List<GameInfo> gameInfos = gameInfoRepository.findAll();
        int totalRedWins = (int) gameInfos.stream()
                .map(GameInfo::getGame)
                .distinct()
                .filter(Game::getRedWin)
                .count();

        int totalGames = (int) gameInfos.stream()
                .map(GameInfo::getGame)
                .distinct()
                .count();
        commonStatisticsRepository.save(CommonStatistic.builder()
                .totalGames(totalGames)
                .totalRedWins(totalRedWins)
                .build());
    }

    public void updateCommonStatistics(Role role) {
        List<CommonStatistic> commonStatisticList = commonStatisticsRepository.findAll();
        CommonStatistic commonStatistic;
        if (commonStatisticList.size() != 0){
            commonStatistic = commonStatisticList.get(0);
        }
        else {
            throw new IndexOutOfBoundsException();
        }
        if (!Role.isBlack(role)) {
            commonStatisticsRepository.save(CommonStatistic.builder()
                    .id(commonStatistic.getId())
                    .totalRedWins(commonStatistic.getTotalRedWins() + 1)
                    .totalGames(commonStatistic.getTotalGames() + 1)
                    .build());
        }
        else {
            commonStatisticsRepository.save(CommonStatistic.builder()
                    .id(commonStatistic.getId())
                    .totalRedWins(commonStatistic.getTotalRedWins())
                    .totalGames(commonStatistic.getTotalGames() + 1)
                    .build());
        }
    }

    public ResponseEntity<CommonWinsDtoResponse> getCommonStatist(){
        List<CommonStatistic> commonStatisticList = commonStatisticsRepository.findAll();
        CommonStatistic commonStatistic;
        if (commonStatisticList.size() != 0){
            commonStatistic = commonStatisticList.get(0);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(CommonWinsDtoResponse.builder()
                .winsByRed(commonStatistic.getTotalRedWins())
                .winsByBlack(commonStatistic.getTotalGames() - commonStatistic.getTotalRedWins())
                .totalGames(commonStatistic.getTotalGames())
                .build(), HttpStatus.OK);
    }


    private Boolean isPlayerWon(Game game, Long playerId) {
        return game.getGameInfos().stream()
                .filter(gameInfo -> gameInfo.getPlayerId().equals(playerId))
                .map(GameInfo::getRole)
                .anyMatch(role -> !Role.isBlack(role) == game.getRedWin());
    }

    private Long getWinsByRoleType(Boolean isRed, List<GameInfo> gameInfos) {
        return isRed ? gameInfos.stream()
                .filter(gameInfo -> !Role.isBlack(gameInfo.getRole()) && gameInfo.getGame().getRedWin())
                .count() : gameInfos.stream()
                .filter(gameInfo -> Role.isBlack(gameInfo.getRole()) && !gameInfo.getGame().getRedWin())
                .count();

    }

    private Long getTotalGamesPlayedByRole(Role role, List<GameInfo> gameInfos) {
        return gameInfos.stream()
                .filter(gameInfo -> gameInfo.getGame().getGameFinished())
                .filter(gameInfo -> gameInfo.getRole().equals(role))
                .count();
    }

    private Long getWinsByRole(Role role, List<GameInfo> gameInfos) {
        return gameInfos.stream()
                .filter(gameInfo -> gameInfo.getGame().getGameFinished())
                .filter(gameInfo -> gameInfo.getRole().equals(role))
                .filter(gameInfo -> (gameInfo.getGame().getRedWin() && !Role.isBlack(role))
                        || (!gameInfo.getGame().getRedWin() && Role.isBlack(role)))
                .count();
    }

    private Long getWinRate(List<GameInfo> gameInfos) {
        double value =
                (double) (getWinsByRoleType(true, gameInfos) + getWinsByRoleType(false, gameInfos)) / gameInfos.size();

        return Math.round(value * 100);
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
                    if (gameInfos.size() < 2) {
                        return null;
                    }
                    long totalWins = getWinsByRoleType(true, gameInfos) + getWinsByRoleType(false, gameInfos);
                    return GameRatingDtoResponse.builder()
                            .playerName(player.getName())
                            .totalWins(totalWins)
                            .totalGames((long) gameInfos.size())
                            .rating(Double.parseDouble(String.format("%.1f",
                                    Math.pow(totalWins, 2) / gameInfos.size()).replaceAll(",", ".")))
                            .build();
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(x ->
                        ((GameRatingDtoResponse) x).getRating())
                        .reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}
