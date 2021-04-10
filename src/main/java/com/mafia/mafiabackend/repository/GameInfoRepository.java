package com.mafia.mafiabackend.repository;

import com.mafia.mafiabackend.model.GameInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GameInfoRepository extends JpaRepository<GameInfo, UUID> {
    Optional<GameInfo> findOneByPlayerIdAndGameId(Long playerId, UUID id);

}
