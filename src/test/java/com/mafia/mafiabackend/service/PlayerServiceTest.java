package com.mafia.mafiabackend.service;

import com.mafia.mafiabackend.model.Player;
import com.mafia.mafiabackend.repository.PlayerRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.AfterMethod;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PlayerServiceTest {

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    MockitoSession session;


    private final List<Player> players = new ArrayList<>();

    @Before
    public void init() {
        session = Mockito.mockitoSession()
                .initMocks(this)
                .startMocking();

        players.add(Player.builder()
                .name("test 1")
                .build());
        players.add(Player.builder()
                .name("test 2")
                .build());
        players.add(Player.builder()
                .name("test 3")
                .build());
        players.add(Player.builder()
                .name("test 4")
                .build());
    }

    @AfterMethod
    public void afterMethod() {
        session.finishMocking();
    }

    @Test
    void createPlayerTest() {
        //mock
        when(playerRepository.save(any())).thenReturn(Player.builder()
                .id(1L)
                .name("test name")
                .build());

        //execute
        long playerId = playerService.addPlayer("test name");

        //assert
        assertEquals(1L, playerId);
    }
}
