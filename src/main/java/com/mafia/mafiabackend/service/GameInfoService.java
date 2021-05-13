package com.mafia.mafiabackend.service;


import com.mafia.mafiabackend.dto.GameFinishDtoRequest;
import com.mafia.mafiabackend.dto.GameInfoDtoRequest;
import com.mafia.mafiabackend.model.*;
import com.mafia.mafiabackend.repository.GameInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class GameInfoService {
    private final GameInfoRepository gameInfoRepository;
    private final GameService gameService;

    public HttpStatus updateGameInfo(GameInfoDtoRequest gameInfoDtoRequest) {
        GameInfo gameInfo = gameInfoRepository.findByPlayerIdAndGameId(
                gameInfoDtoRequest.getPlayerId(),
                gameInfoDtoRequest.getId())
                .orElseThrow(RuntimeException::new);
        gameInfo.setFoul(gameInfoDtoRequest.getFouls());
        gameInfo.setAlive(gameInfoDtoRequest.getAlive());
        if (gameInfoDtoRequest.getPoints() != null) {
            gameInfo.setPoints(gameInfoDtoRequest.getPoints());
        }
        gameInfo.getMonitoringInfo().setUpdatedAt(Instant.now());
        gameInfoRepository.save(gameInfo);

        Game game = gameInfo.getGame();

        boolean gameFinishedByBlack = isGameFinishedByBlack(game);
        boolean gameFinishedByRed = isGameFinishedByRed(game);
        if (gameFinishedByBlack) {
            gameService.finishGame(GameFinishDtoRequest.builder()
                    .id(game.getId())
                    .result(GameResult.BLACK_WIN)
                    .build());
        }
        if (gameFinishedByRed) {
            gameService.finishGame(GameFinishDtoRequest.builder()
                    .id(game.getId())
                    .result(GameResult.RED_WIN)
                    .build());
        }

        return HttpStatus.OK;
    }

    private boolean isGameFinishedByRed(Game game) {
        List<GameInfo> gameInfos = game.getGameInfos();
        long numberOfAliveBlackPlayers = gameInfos.stream()
                .filter(x -> Role.isBlack(x.getRole()) && x.getAlive())
                .count();
        return numberOfAliveBlackPlayers == 0;
    }

    private boolean isGameFinishedByBlack(Game game) {
        List<GameInfo> gameInfos = game.getGameInfos();
        long numberOfAliveBlackPlayers = gameInfos.stream()
                .filter(x -> Role.isBlack(x.getRole()) && x.getAlive())
                .count();
        long numberOfAliveRedPlayers = gameInfos.stream()
                .filter(x -> !Role.isBlack(x.getRole()) && x.getAlive())
                .count();
        return numberOfAliveBlackPlayers == numberOfAliveRedPlayers;
    }
}
