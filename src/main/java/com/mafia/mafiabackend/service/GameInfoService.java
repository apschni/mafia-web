package com.mafia.mafiabackend.service;


import com.mafia.mafiabackend.dto.GameInfoDtoRequest;
import com.mafia.mafiabackend.model.GameInfo;
import com.mafia.mafiabackend.repository.GameInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GameInfoService {
    private final GameInfoRepository gameInfoRepository;

    public GameInfo updateGameInfo(GameInfoDtoRequest gameInfoDtoRequest) {
        GameInfo gameInfo = gameInfoRepository.findOneByPlayerIdAndGameId(
                gameInfoDtoRequest.getPlayerId(),
                gameInfoDtoRequest.getId())
                .orElseThrow(RuntimeException::new);
        gameInfo.setFoul(gameInfoDtoRequest.getFouls());
        gameInfo.setAlive(gameInfoDtoRequest.getAlive());
        gameInfoRepository.save(gameInfo);
        return gameInfo;
    }
}
