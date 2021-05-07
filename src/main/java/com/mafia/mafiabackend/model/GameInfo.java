package com.mafia.mafiabackend.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Schema(hidden = true)
@Entity
@Data
@ToString(exclude = {"game", "player"})
@EqualsAndHashCode(exclude = {"game", "player"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameInfo {
    @GeneratedValue
    @JsonIgnore
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    @JsonBackReference
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    @JsonBackReference
    private Game game;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Integer points;

    private Integer sitNumber;

    private Boolean alive;

    private Integer foul;

    @Column(name = "game_id", insertable = false, updatable = false)
    private Long gameId;


    @Column(name = "player_id", insertable = false, updatable = false)
    private Long playerId;


}

