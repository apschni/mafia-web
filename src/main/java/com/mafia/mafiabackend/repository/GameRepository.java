package com.mafia.mafiabackend.repository;

import com.mafia.mafiabackend.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByGameFinishedFalse();
    List<Game> findAllByGameFinishedTrue();
}
