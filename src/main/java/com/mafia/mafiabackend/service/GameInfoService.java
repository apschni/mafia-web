package com.mafia.mafiabackend.service;


import com.mafia.mafiabackend.dto.GameFinishDtoRequest;
import com.mafia.mafiabackend.dto.GameInfoDtoRequest;
import com.mafia.mafiabackend.dto.GameInfoDtoResponse;
import com.mafia.mafiabackend.model.Game;
import com.mafia.mafiabackend.model.GameInfo;
import com.mafia.mafiabackend.model.Role;
import com.mafia.mafiabackend.repository.GameInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class GameInfoService {
    private final GameInfoRepository gameInfoRepository;
    private final GameService gameService;

    public GameInfoDtoResponse getGameInfos(Long id) {
        List<GameInfo> gameInfos = gameInfoRepository.findAllByGameId(id);
        Set<GameInfo> targetSet = new HashSet<>(gameInfos);
        return GameInfoDtoResponse.builder()
                .gameInfos(targetSet)
                .gameFinished(false)
                .build();
    }

    public GameInfoDtoResponse updateGameInfo(GameInfoDtoRequest gameInfoDtoRequest) {
        GameInfo gameInfo = gameInfoRepository.findByPlayerIdAndGameId(
                gameInfoDtoRequest.getPlayerId(),
                gameInfoDtoRequest.getId())
                .orElseThrow(RuntimeException::new);
        gameInfo.setFoul(gameInfoDtoRequest.getFouls());
        gameInfo.setAlive(gameInfoDtoRequest.getAlive());
        if (gameInfoDtoRequest.getPoints() != null) {
            gameInfo.setPoints(gameInfoDtoRequest.getPoints());
        }
        gameInfoRepository.save(gameInfo);

        Game game = gameInfo.getGame();
        if (isGameFinishedByBlack(game)) {
            gameService.finishGame(GameFinishDtoRequest.builder()
                    .id(game.getId())
                    .redWin(false)
                    .build());
        }
        if (isGameFinishedByRed(game)) {
            gameService.finishGame(GameFinishDtoRequest.builder()
                    .id(game.getId())
                    .redWin(false)
                    .build());
        }

        return GameInfoDtoResponse.builder()
//                .gameInfos(game.getGameInfos())
                .gameInfo(gameInfo)
                .gameFinished(isGameFinishedByBlack(game))
                .build();
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
