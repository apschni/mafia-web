package com.mafia.mafiabackend.model;


import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Hidden
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameInfo {
    @GeneratedValue
    @Id
    private UUID id;

    @ManyToOne
    private Player player;

    @ManyToOne
    private Game game;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Integer points;

    private Integer sitNumber;

    private Boolean alive;

    private Integer foul;
}

