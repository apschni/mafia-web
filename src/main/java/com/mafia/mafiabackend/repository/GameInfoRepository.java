package com.mafia.mafiabackend.repository;

import com.mafia.mafiabackend.model.GameInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameInfoRepository extends JpaRepository<GameInfo, Long> {
    Optional<GameInfo> findByPlayerIdAndGameId(Long playerId, Long id);

    List<GameInfo> findAllByGameId(Long id);
}
