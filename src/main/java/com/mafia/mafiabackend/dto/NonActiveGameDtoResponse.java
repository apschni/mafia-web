package com.mafia.mafiabackend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NonActiveGameDtoResponse {
    private Long gameId;
    private List<String> playerNames;
    private Boolean winByRed;
}
