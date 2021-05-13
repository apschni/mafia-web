package com.mafia.mafiabackend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ActiveGamesDtoResponse {
    private Long gameId;
    private List<String> playerNames;
}
