package com.mafia.mafiabackend.service;

import com.mafia.mafiabackend.dto.PlayerDtoResponse;
import com.mafia.mafiabackend.model.Player;
import com.mafia.mafiabackend.repository.PlayerRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.testng.annotations.AfterMethod;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
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
    void createPlayerAlreadyExistsTest() {
        when(playerRepository.existsByName(any())).thenReturn(true);

        Long playerId = playerService.addPlayer("Test Name");

        Assertions.assertNull(playerId);
    }

    @Test
    void createPlayerTest() {
        when(playerRepository.existsByName(any())).thenReturn(false);
        when(playerRepository.save(any())).thenReturn(Player.builder()
                .id(1L)
                .name("Test Name")
                .build());

        Long playerId = playerService.addPlayer("Test Name");

        Assertions.assertEquals(1L, playerId);
    }


//    @Test
//    void getAllPlayersOrderedByTotalGamesPlayedTest(){
//        when(playerRepository.findAll()).thenReturn(List.of(
//                new
//        ))
//    }

//    @Test
//    void getPlayerById() {
//        when(playerRepository.findById(any())).thenReturn(Optional.of(Player.builder()
//                .id(1L)
//                .name("Test Name")
//                .build()));
//
//        when(conversionService.convert(any(Player.class), )).
//
//        PlayerDtoResponse playerDtoResponse = playerService.getPlayerById(1L);
//
//        Assertions.assertEquals(PlayerDtoResponse.builder()
//                .name("Test Name")
//                .id(1L)
//                .build(), playerDtoResponse);
//    }
}
