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
public class Player {

    @Id
    @SequenceGenerator(name = "playerSec", sequenceName = "PLAYER_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "playerSec")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<GameInfo> gameInfos;
}
