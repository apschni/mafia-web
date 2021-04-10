package com.mafia.mafiabackend.model;


import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Hidden
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {


    @Id
    @GeneratedValue
    private Long id;

    private String name;

}
