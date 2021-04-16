package com.mafia.mafiabackend.service;

import com.mafia.mafiabackend.dto.GameDtoRequest;
import com.mafia.mafiabackend.model.Game;
import com.mafia.mafiabackend.model.GameInfo;
import com.mafia.mafiabackend.model.GameType;
import com.mafia.mafiabackend.model.Role;
import com.mafia.mafiabackend.repository.GameInfoRepository;
import com.mafia.mafiabackend.repository.GameRepository;
import com.mafia.mafiabackend.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final GameInfoRepository gameInfoRepository;
    private final PlayerRepository playerRepository;

    public Game createGame(GameDtoRequest gameDtoRequest) {
        Game game = Game.builder()
                .gameType(gameDtoRequest.getGameType())
                .numberOfPlayers(gameDtoRequest.getPlayersIds().size())
                .build();
        gameRepository.save(game);

        Map<Long, Role> playerIdToRole = new HashMap<>();
        randomizeRoles(gameDtoRequest, playerIdToRole, new ArrayList<>(gameDtoRequest.getPlayersIds()));


        List<GameInfo> gameInfos = new ArrayList<>();
        List<Long> playersIds = gameDtoRequest.getPlayersIds();
        for (int i = 0; i < playersIds.size(); i++) {
            Long id = playersIds.get(i);
            GameInfo gameInfo = GameInfo.builder()
                    .game(game)
                    .alive(true)
                    .foul(0)
                    .points(0)
                    .sitNumber(i + 1)
                    .role(playerIdToRole.get(id))
                    .player(playerRepository.findById(id).orElse(null))
                    .build();
            gameInfos.add(gameInfo);
        }
        gameInfoRepository.saveAll(gameInfos);
        return game;
    }


    private void randomizeRoles(GameDtoRequest gameDtoRequest, Map<Long, Role> playerIdToRole, List<Long> playersIds) {
        int counter = 0;
        if (gameDtoRequest.getGameType() == GameType.KIEV) {
            Long idToRemove = playersIds.get((int) (Math.random() * playersIds.size()));
            playerIdToRole.put(idToRemove, Role.WHORE);
            counter++;
            playersIds.remove(idToRemove);
            idToRemove = playersIds.get((int) (Math.random() * playersIds.size()));
            playerIdToRole.put(idToRemove, Role.DOCTOR);
            playersIds.remove(idToRemove);
            idToRemove = playersIds.get((int) (Math.random() * playersIds.size()));
            playerIdToRole.put(idToRemove, Role.MANIAC);
            playersIds.remove(idToRemove);
        }
        Long idToRemove = playersIds.get((int) (Math.random() * playersIds.size()));
        playerIdToRole.put(idToRemove, Role.SHERIFF);
        playersIds.remove(idToRemove);
        idToRemove = playersIds.get((int) (Math.random() * playersIds.size()));
        playerIdToRole.put(idToRemove, Role.DON);
        playersIds.remove(idToRemove);
        counter++;
        while (counter < gameDtoRequest.getPlayersIds().size() / 3) {
            idToRemove = playersIds.get((int) (Math.random() * playersIds.size()));
            playerIdToRole.put(idToRemove, Role.BLACK);
            playersIds.remove(idToRemove);
            counter++;
        }
        for (Long id : playersIds) {
            playerIdToRole.put(id, Role.RED);
        }
    }
}
