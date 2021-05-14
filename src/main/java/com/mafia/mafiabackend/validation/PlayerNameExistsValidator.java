package com.mafia.mafiabackend.validation;

import com.mafia.mafiabackend.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class PlayerNameExistsValidator implements ConstraintValidator<PlayerNameExists, String> {

    private final PlayerRepository playerRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !playerRepository.existsByName(value);
    }
}
