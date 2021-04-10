package com.mafia.mafiabackend.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

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

