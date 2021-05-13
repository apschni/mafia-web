package com.mafia.mafiabackend.converter;

import com.mafia.mafiabackend.dto.PlayerDtoResponse;
import com.mafia.mafiabackend.model.Player;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PlayerToPlayerDtoResponseConverter implements Converter<Player, PlayerDtoResponse> {

    @Override
    public PlayerDtoResponse convert(Player player) {
        return PlayerDtoResponse.builder()
                .id(player.getId())
                .name(player.getName())
                .build();
    }

}
