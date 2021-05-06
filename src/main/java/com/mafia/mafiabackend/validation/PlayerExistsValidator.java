package com.mafia.mafiabackend.validation;

import com.mafia.mafiabackend.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class PlayerExistsValidator implements ConstraintValidator<PlayerExists, Long> {

    private final PlayerRepository playerRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {
        return playerRepository.existsById(value);
    }

}
