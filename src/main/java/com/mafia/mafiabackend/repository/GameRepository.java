package com.mafia.mafiabackend.repository;

import com.mafia.mafiabackend.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByRedWinIsNull();
}
