package com.mafia.mafiabackend.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Hidden
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    @SequenceGenerator(name = "gameSec", sequenceName = "GAME_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gameSec")
    private Long id;

    @Enumerated(EnumType.STRING)
    private GameType gameType;

    private Boolean redWin;

    private Boolean gameFinished;

    private Integer numberOfPlayers;

    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<GameInfo> gameInfos;

}
