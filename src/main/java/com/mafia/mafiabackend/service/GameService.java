package com.mafia.mafiabackend.service;

import com.mafia.mafiabackend.dto.GameDtoRequest;
import com.mafia.mafiabackend.dto.GameFinishDtoRequest;
import com.mafia.mafiabackend.model.*;
import com.mafia.mafiabackend.repository.GameInfoRepository;
import com.mafia.mafiabackend.repository.GameRepository;
import com.mafia.mafiabackend.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class GameService {
    private final GameRepository gameRepository;
    private final GameInfoRepository gameInfoRepository;
    private final PlayerRepository playerRepository;

    public List<Long> getActiveGames() {
        List<Game> games = gameRepository.findAllByGameFinishedFalse();
        List<Long> gameIds = new ArrayList<>();
        for (Game game : games) {
            gameIds.add(game.getId());
        }
        return gameIds;
    }

    public Long finishGame(GameFinishDtoRequest gameFinishDtoRequest) {
        Game game = gameRepository.findById(gameFinishDtoRequest.getId()).orElse(null);

        if (game == null) {
            log.error("Failed to finish game with id: "
                    + gameFinishDtoRequest.getId()
                    + " (no such game in database with id: " + gameFinishDtoRequest.getId() + ")");
            return null;
        }

        if (gameFinishDtoRequest.getRedWin() == 2){
            gameInfoRepository.deleteAll(game.getGameInfos());
            gameRepository.deleteById(gameFinishDtoRequest.getId());
            log.info("Game with id: " + gameFinishDtoRequest.getId() + " has been deleted from database");
            return gameFinishDtoRequest.getId();
        }

        game.setRedWin(gameFinishDtoRequest.getRedWin() != 0);
        game.setGameFinished(true);
        gameRepository.save(game);
        log.info("Game with id: " + gameFinishDtoRequest.getId() + " has been finished");
        return game.getId();
    }

    public Long createGame(GameDtoRequest gameDtoRequest) {
        Game game = Game.builder()
                .gameType(gameDtoRequest.getGameType())
                .numberOfPlayers(gameDtoRequest.getPlayersIds().size())
                .gameFinished(false)
                .build();
        gameRepository.save(game);

        Map<Long, Role> playerIdToRole = new HashMap<>();
        randomizeRoles(gameDtoRequest.getGameType(),
                gameDtoRequest.getPlayersIds().size(),
                playerIdToRole,
                new ArrayList<>(gameDtoRequest.getPlayersIds()));


        List<GameInfo> gameInfos = new ArrayList<>();
        List<Long> playersIds = gameDtoRequest.getPlayersIds();
        Map<Long, Player> idToPlayer = playerRepository.findAllById(playersIds).stream()
                .collect(Collectors.toMap(Player::getId, Function.identity()));
        for (int i = 0; i < playersIds.size(); i++) {
            Long id = playersIds.get(i);
            GameInfo gameInfo = GameInfo.builder()
                    .game(game)
                    .alive(true)
                    .foul(0)
                    .points(0)
                    .sitNumber(i + 1)
                    .role(playerIdToRole.get(id))
                    .player(idToPlayer.get(id))
                    .build();
            gameInfos.add(gameInfo);
        }
        gameInfoRepository.saveAll(gameInfos);
        log.info("Game with id: " + game.getId() + "and game type: " + game.getGameType() + " has been created");
        return game.getId();
    }


    public Long reshuffleRoles(Long id, GameType gameType) {
        List<GameInfo> gameInfos = gameInfoRepository.findAllByGameId(id);
        List<Long> playerIds = gameInfos.stream()
                .map(GameInfo::getPlayerId)
                .collect(Collectors.toList());

        Map<Long, Role> playerIdToRole = new HashMap<>();
        randomizeRoles(gameType, gameInfos.size(), playerIdToRole, playerIds);

        gameInfos.forEach(gameInfo -> {
            Long playerId = gameInfo.getPlayerId();
            Role role = playerIdToRole.get(playerId);
            gameInfo.setRole(role);
        });
        gameInfoRepository.saveAll(gameInfos);
        return id;
    }

    private void randomizeRoles(GameType gameType,
                                Integer numberOfPlayers,
                                Map<Long, Role> playerIdToRole,
                                List<Long> playersIds) {
//    private void randomizeRoles(GameDtoRequest gameDtoRequest, Map<Long, Role> playerIdToRole, List<Long> playersIds) {
        int counter = 0;
        if (gameType == GameType.KIEV) {
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
        while (counter < numberOfPlayers / 3) {
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
