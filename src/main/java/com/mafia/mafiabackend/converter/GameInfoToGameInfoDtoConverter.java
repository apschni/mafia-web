package com.mafia.mafiabackend.converter;

import com.mafia.mafiabackend.dto.GameInfoDto;
import com.mafia.mafiabackend.model.GameInfo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GameInfoToGameInfoDtoConverter implements Converter<GameInfo, GameInfoDto> {

    @Override
    public GameInfoDto convert(GameInfo gameInfo) {
        return GameInfoDto.builder()
                .playerId(gameInfo.getPlayer().getId())
                .playerName(gameInfo.getPlayer().getName())
                .alive(gameInfo.getAlive())
                .foul(gameInfo.getFoul())
                .points(gameInfo.getPoints())
                .role(gameInfo.getRole())
                .sitNumber(gameInfo.getSitNumber())
                .build();
    }

}
