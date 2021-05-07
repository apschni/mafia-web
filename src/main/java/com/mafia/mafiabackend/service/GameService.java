package com.mafia.mafiabackend.service;

import com.mafia.mafiabackend.dto.*;
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


    public GameInfoDtoResponse getGameInfos(Long id) {
        List<GameInfo> gameInfos = gameInfoRepository.findAllByGameId(id);
        if (gameInfos.isEmpty()) {
            return null;
        }

        List<GameInfoInnerDto> gameInfoInnerDtos = new ArrayList<>();

        for (GameInfo gameInfo : gameInfos) {
            gameInfoInnerDtos.add(GameInfoInnerDto.builder()
                    .gameInfo(gameInfo)
                    .playerName(gameInfo.getPlayer().getName())
                    .build());
        }
        return GameInfoDtoResponse.builder()
                .playerInfos(gameInfoInnerDtos)
                .gameFinished(false)
                .build();
    }

    public List<ActiveGamesDtoResponse> getActiveGames() {
        List<Game> games = gameRepository.findAllByGameFinishedFalse();
        List<ActiveGamesDtoResponse> activeGamesDtoResponses = new ArrayList<>();
        games.forEach(game -> {
            activeGamesDtoResponses.add(ActiveGamesDtoResponse.builder()
                    .gameId(game.getId())
                    .playerNames(game.getGameInfos().stream()
                            .map(GameInfo::getPlayer)
                            .map(Player::getName)
                            .collect(Collectors.toList()))
                    .build());
        });

        return activeGamesDtoResponses;
    }

    public Long finishGame(GameFinishDtoRequest gameFinishDtoRequest) {
        Game game = gameRepository.findById(gameFinishDtoRequest.getId()).orElse(null);

        if (game == null) {
            log.error("Failed to finish game with id: "
                    + gameFinishDtoRequest.getId()
                    + " (no such game in database with id: " + gameFinishDtoRequest.getId() + ")");
            return null;
        }

        if (gameFinishDtoRequest.getResult() == GameResult.SKIP_AND_DELETE) {
            gameInfoRepository.deleteAll(game.getGameInfos());
            gameRepository.deleteById(gameFinishDtoRequest.getId());
            log.info("Game with id: " + gameFinishDtoRequest.getId() + " has been deleted from database");
            return gameFinishDtoRequest.getId();
        }

        game.setRedWin(gameFinishDtoRequest.getResult() == GameResult.RED_WIN);
        game.setGameFinished(true);
        gameRepository.save(game);
        log.info("Game with id: " + gameFinishDtoRequest.getId() + " has been finished");
        return game.getId();
    }

    public GameInfoDtoResponse createGame(GameDtoRequest gameDtoRequest) {
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


        List<GameInfoInnerDto> gameInfoInnerDtos = gameInfoSaveAndCreateDto(gameInfos);
        log.info("Game with id: " + game.getId() + "and game type: " + game.getGameType() + " has been created");
        return GameInfoDtoResponse.builder()
                .playerInfos(gameInfoInnerDtos)
                .gameFinished(false)
                .build();
    }


    public GameInfoDtoResponse reshuffleRoles(Long id, GameType gameType) {
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

        List<GameInfoInnerDto> gameInfoInnerDtos = gameInfoSaveAndCreateDto(gameInfos);
        return GameInfoDtoResponse.builder()
                .playerInfos(gameInfoInnerDtos)
                .gameFinished(false)
                .build();
    }

    private List<GameInfoInnerDto> gameInfoSaveAndCreateDto(List<GameInfo> gameInfos) {
        List<GameInfo> gameInfos1 = gameInfoRepository.saveAll(gameInfos);

        List<GameInfoInnerDto> gameInfoInnerDtos = new ArrayList<>();
        for (GameInfo gameInfo1 : gameInfos1) {
            gameInfoInnerDtos.add(GameInfoInnerDto.builder()
                    .gameInfo(gameInfo1)
                    .playerName(gameInfo1.getPlayer().getName())
                    .build());
        }
        return gameInfoInnerDtos;
    }

    private void randomizeRoles(GameType gameType,
                                Integer numberOfPlayers,
                                Map<Long, Role> playerIdToRole,
                                List<Long> playersIds) {
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
