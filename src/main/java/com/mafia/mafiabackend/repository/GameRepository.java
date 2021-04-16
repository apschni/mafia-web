package com.mafia.mafiabackend.repository;

import com.mafia.mafiabackend.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
