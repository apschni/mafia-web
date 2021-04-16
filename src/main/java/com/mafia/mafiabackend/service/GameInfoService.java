package com.mafia.mafiabackend.service;


import com.mafia.mafiabackend.dto.GameInfoDtoRequest;
import com.mafia.mafiabackend.dto.GameInfoDtoResponse;
import com.mafia.mafiabackend.model.Game;
import com.mafia.mafiabackend.model.GameInfo;
import com.mafia.mafiabackend.model.Role;
import com.mafia.mafiabackend.repository.GameInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class GameInfoService {
    private final GameInfoRepository gameInfoRepository;

    public GameInfoDtoResponse updateGameInfo(GameInfoDtoRequest gameInfoDtoRequest) {
        GameInfo gameInfo = gameInfoRepository.findOneByPlayerIdAndGameId(
                gameInfoDtoRequest.getPlayerId(),
                gameInfoDtoRequest.getId())
                .orElseThrow(RuntimeException::new);
        gameInfo.setFoul(gameInfoDtoRequest.getFouls());
        gameInfo.setAlive(gameInfoDtoRequest.getAlive());
        if (gameInfoDtoRequest.getPoints() != null){
            gameInfo.setPoints(gameInfoDtoRequest.getPoints());
        }
        gameInfoRepository.save(gameInfo);
        Game game = gameInfo.getGame();
        return GameInfoDtoResponse.builder()
                .gameInfos(game.getGameInfos())
                .gameFinished(isGameFinished(game))
                .build();
    }

    private boolean isGameFinished(Game game) {
        Set<GameInfo> gameInfos = game.getGameInfos();
        long numberOfAliveBlackPlayers = gameInfos.stream()
                .filter(x -> Role.isBlack(x.getRole()) && x.getAlive())
                .count();
        long numberOfAliveRedPlayers = gameInfos.stream()
                .filter(x -> !Role.isBlack(x.getRole()) && x.getAlive())
                .count();
        return numberOfAliveBlackPlayers == numberOfAliveRedPlayers;
    }
}
