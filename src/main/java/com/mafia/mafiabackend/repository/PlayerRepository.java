package com.mafia.mafiabackend.repository;

import com.mafia.mafiabackend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Boolean existsByName(String name);
}
