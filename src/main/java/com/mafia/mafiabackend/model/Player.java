package com.mafia.mafiabackend.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Hidden
@Entity
@Data
@ToString(exclude = "gameInfos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @SequenceGenerator(name = "playerSec", sequenceName = "PLAYER_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "playerSec")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<GameInfo> gameInfos;
}
