package com.mafia.mafiabackend.service;

import com.mafia.mafiabackend.converter.PlayerToPlayerDtoResponseConverter;
import com.mafia.mafiabackend.dto.PlayerDtoResponse;
import com.mafia.mafiabackend.model.Game;
import com.mafia.mafiabackend.model.GameInfo;
import com.mafia.mafiabackend.model.Player;
import com.mafia.mafiabackend.repository.PlayerRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.testng.annotations.AfterMethod;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private ConversionService conversionService;

    @Mock
    private PlayerRepository playerRepository;

    MockitoSession session;


    @Before
    public void init() {
        session = Mockito.mockitoSession()
                .initMocks(this)
                .startMocking();
    }

    @AfterMethod
    public void afterMethod() {
        session.finishMocking();
    }

    @Test
    void createPlayerAlreadyExists() {
        when(playerRepository.existsByName(any())).thenReturn(true);

        Long playerId = playerService.addPlayer("Test Name");

        verify(playerRepository, never()).save(any());
        Assertions.assertNull(playerId);
    }

    @Test
    void createPlayer() {
        when(playerRepository.existsByName(any())).thenReturn(false);
        when(playerRepository.save(any())).thenReturn(Player.builder()
                .id(1L)
                .name("Test Name")
                .build());

        Long playerId = playerService.addPlayer("Test Name");

        verify(playerRepository, times(1)).save(any());
        Assertions.assertEquals(1L, playerId);
    }


    @Test
    void getPlayerById() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(Player.builder()
                .id(1L)
                .name("Test Name")
                .build()));

        when(conversionService.convert(any(), any())).thenReturn(PlayerDtoResponse.builder()
                .id(1L)
                .name("Test Name")
                .build());

        PlayerDtoResponse playerDtoResponse = playerService.getPlayerById(1L);

        Assertions.assertEquals(PlayerDtoResponse.builder()
                .name("Test Name")
                .id(1L)
                .build(), playerDtoResponse);
    }

    @Test
    void getPlayerByIdAlreadyExists() {
        when(playerRepository.findById(any())).thenReturn(Optional.empty());

        PlayerDtoResponse playerDtoResponse = playerService.getPlayerById(any());

        Assertions.assertNull(playerDtoResponse);
    }

    @Test
    void deletePlayerById() {
        when(playerRepository.existsById(1L)).thenReturn(true);

        playerService.deletePlayerById(1L);

        verify(playerRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePlayerByIdNotExists() {
        when(playerRepository.existsById(1L)).thenReturn(false);

        playerService.deletePlayerById(1L);

        verify(playerRepository, never()).deleteById(any());
    }

    @Test
    void getAllPlayersOrderedByTotalGamesPlayed() {
        List<Player> players = Arrays.asList(
                Player.builder()
                        .id(1L)
                        .name("Test name 1")
                        .gameInfos(Arrays.asList(
                                GameInfo.builder()
                                        .game(Game.builder()
                                                .gameFinished(true)
                                                .build())
                                        .build(),
                                GameInfo.builder()
                                        .game(Game.builder()
                                                .gameFinished(true)
                                                .build())
                                        .build()
                        ))
                        .build(),
                Player.builder()
                        .id(2L)
                        .name("Test name 2")
                        .gameInfos(Arrays.asList(
                                GameInfo.builder()
                                        .game(Game.builder()
                                                .gameFinished(true)
                                                .build())
                                        .build(),
                                GameInfo.builder()
                                        .game(Game.builder()
                                                .gameFinished(true)
                                                .build())
                                        .build(),
                                GameInfo.builder()
                                        .game(Game.builder()
                                                .gameFinished(true)
                                                .build())
                                        .build()
                                ))
                        .build()
        );

        when(playerRepository.findAll()).thenReturn(players);

        when(conversionService.convert(players.get(0), PlayerDtoResponse.class)).thenReturn(PlayerDtoResponse.builder()
                .id(1L)
                .name("Test name 1")
                .build());

        when(conversionService.convert(players.get(1), PlayerDtoResponse.class)).thenReturn(PlayerDtoResponse.builder()
                .id(2L)
                .name("Test name 2")
                .build());


        List<PlayerDtoResponse> allPlayersOrderedByTotalGamesPlayed =
                playerService.getAllPlayersOrderedByTotalGamesPlayed();

        Assertions.assertEquals("Test name 2", allPlayersOrderedByTotalGamesPlayed.get(0).getName());
    }
}
