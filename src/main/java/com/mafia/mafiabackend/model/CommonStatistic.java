package com.mafia.mafiabackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonStatistic {
    @Id
    @GeneratedValue
    private Long id;

    private Integer totalRedWins;
    private Integer totalGames;
}
