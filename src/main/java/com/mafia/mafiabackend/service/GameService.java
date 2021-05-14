package com.mafia.mafiabackend.service;

import com.mafia.mafiabackend.dto.*;
import com.mafia.mafiabackend.model.*;
import com.mafia.mafiabackend.repository.GameInfoRepository;
import com.mafia.mafiabackend.repository.GameRepository;
import com.mafia.mafiabackend.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class GameService {

    private final GameRepository gameRepository;
    private final GameInfoRepository gameInfoRepository;
    private final PlayerRepository playerRepository;
    private final ConversionService conversionService;

    public GameInfoDtoResponse getGameInfosByGameId(Long id) {
        List<GameInfo> gameInfos = gameInfoRepository.findAllByGameId(id).stream()
                .sorted(Comparator.comparing(GameInfo::getAlive).reversed().thenComparing(GameInfo::getSitNumber))
                .collect(Collectors.toList());
        if (gameInfos.isEmpty()) {
            return null;
        }

        List<GameInfoDto> gameInfoDtos = new ArrayList<>();

        for (GameInfo gameInfo : gameInfos) {
            gameInfoDtos.add(conversionService.convert(gameInfo, GameInfoDto.class));
        }
        Optional<Game> optionalGame = gameRepository.findById(id);
        boolean gameStarted = false;
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            gameStarted = game.getGameStarted();
        }
        return GameInfoDtoResponse.builder()
                .playerInfos(gameInfoDtos)
                .gameFinished(false)
                .gameId(id)
                .gameStarted(gameStarted)
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

    public List<NonActiveGameDtoResponse> getLastTenNonActiveGames() {
        List<Game> games = gameRepository.findAllByGameFinishedTrue();
        List<NonActiveGameDtoResponse> nonActiveGameDtoResponses = new ArrayList<>();
        games.stream()
                .sorted(Comparator.comparing(x -> ((Game) x).getMonitoringInfo().getUpdatedAt()).reversed())
                .limit(10)
                .forEach(game -> {
                    nonActiveGameDtoResponses.add(NonActiveGameDtoResponse.builder()
                            .gameId(game.getId())
                            .playerNames(game.getGameInfos().stream()
                                    .map(GameInfo::getPlayer)
                                    .map(Player::getName)
                                    .collect(Collectors.toList()))
                            .winByRed(game.getRedWin())
                            .build());
                });
        return nonActiveGameDtoResponses;
    }

    public HttpStatus finishGame(GameFinishDtoRequest gameFinishDtoRequest) {
        Optional<Game> optionalGame = gameRepository.findById(gameFinishDtoRequest.getId());

        if (!optionalGame.isPresent()) {
            return HttpStatus.NOT_FOUND;
        }
        Game game = optionalGame.get();
        if (game.getGameFinished()) {
            return HttpStatus.ALREADY_REPORTED;
        }

        if (gameFinishDtoRequest.getResult() == GameResult.SKIP_AND_DELETE) {
            gameInfoRepository.deleteAll(game.getGameInfos());
            gameRepository.deleteById(gameFinishDtoRequest.getId());
            log.info("Game with id: " + gameFinishDtoRequest.getId() + " has been deleted from database");
            return HttpStatus.OK;
        }

        game.setRedWin(gameFinishDtoRequest.getResult() == GameResult.RED_WIN);
        game.setGameFinished(true);
        game.getMonitoringInfo().setUpdatedAt(Instant.now());
        gameRepository.save(game);
        log.info("Game with id: " + gameFinishDtoRequest.getId() + " has been finished");
        return HttpStatus.OK;
    }

    public Long createGame(GameDtoRequest gameDtoRequest) {
        Game game = Game.builder()
                .gameType(gameDtoRequest.getGameType())
                .numberOfPlayers(gameDtoRequest.getPlayersIds().size())
                .gameFinished(false)
                .gameStarted(false)
                .monitoringInfo(MonitoringInfo.builder()
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build())
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
            Player player = idToPlayer.get(id);
            GameInfo gameInfo = GameInfo.builder()
                    .game(game)
                    .alive(true)
                    .foul(0)
                    .points(0)
                    .sitNumber(i + 1)
                    .role(playerIdToRole.get(id))
                    .player(player)
                    .monitoringInfo(MonitoringInfo.builder()
                            .createdAt(Instant.now())
                            .updatedAt(Instant.now())
                            .build())
                    .build();
            gameInfos.add(gameInfo);
        }


        gameInfoRepository.saveAll(gameInfos);
        log.info("Game with id: " + game.getId() + "and game type: " + game.getGameType() + " has been created");
        return game.getId();
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
            gameInfo.getMonitoringInfo().setUpdatedAt(Instant.now());
        });

        List<GameInfoDto> gameInfoDtos = gameInfoSaveAndCreateDto(gameInfos);
        return GameInfoDtoResponse.builder()
                .playerInfos(gameInfoDtos)
                .gameFinished(false)
                .gameId(id)
                .gameStarted(false)
                .build();
    }

    private List<GameInfoDto> gameInfoSaveAndCreateDto(List<GameInfo> gameInfos) {

        return gameInfoRepository.saveAll(gameInfos).stream()
                .sorted(Comparator.comparing(GameInfo::getSitNumber))
                .map(gameInfo -> conversionService.convert(gameInfo, GameInfoDto.class))
                .collect(Collectors.toList());
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

    public void startGame(Long gameId) {
        Optional<Game> optionalGame = gameRepository.findById(gameId);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            game.setGameStarted(true);
            gameRepository.save(game);
        }
    }
}
