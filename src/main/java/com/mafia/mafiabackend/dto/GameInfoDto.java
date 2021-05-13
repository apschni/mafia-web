package com.mafia.mafiabackend.dto;

import com.mafia.mafiabackend.model.Role;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GameInfoDto {

    private Long playerId;

    private String playerName;

    private Role role;

    private Integer points;

    private Integer sitNumber;

    private Boolean alive;

    private Integer foul;
}
