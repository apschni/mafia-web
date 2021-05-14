package com.mafia.mafiabackend.service;

import com.mafia.mafiabackend.dto.GameFinishDtoRequest;
import com.mafia.mafiabackend.dto.GameInfoDtoRequest;
import com.mafia.mafiabackend.model.Game;
import com.mafia.mafiabackend.model.GameInfo;
import com.mafia.mafiabackend.model.GameResult;
import com.mafia.mafiabackend.model.Role;
import com.mafia.mafiabackend.repository.GameInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class GameInfoService {
    private final GameInfoRepository gameInfoRepository;
    private final GameService gameService;

    public GameResult updateGameInfo(GameInfoDtoRequest gameInfoDtoRequest) {
        GameInfo gameInfo = gameInfoRepository.findByPlayerIdAndGameId(
                gameInfoDtoRequest.getPlayerId(),
                gameInfoDtoRequest.getId())
                .orElseThrow(RuntimeException::new);
        if (gameInfo.getGame().getGameFinished()) {
            return GameResult.GAME_ALREADY_FINISHED;
        }

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
            return GameResult.BLACK_WIN;
        }
        if (gameFinishedByRed) {
            gameService.finishGame(GameFinishDtoRequest.builder()
                    .id(game.getId())
                    .result(GameResult.RED_WIN)
                    .build());
            return GameResult.RED_WIN;
        }
        return GameResult.GAME_IN_PROGRESS;
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
