package com.mafia.mafiabackend.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

import javax.persistence.*;

@Hidden
@Entity
@Data
@ToString(exclude = {"game", "player"})
@EqualsAndHashCode(exclude = {"game", "player"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameInfo {
    @GeneratedValue
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    @JsonBackReference
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    @JsonBackReference
    private Game game;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Integer points;

    private Integer sitNumber;

    private Boolean alive;

    private Integer foul;
}

