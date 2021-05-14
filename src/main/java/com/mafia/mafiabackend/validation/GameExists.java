package com.mafia.mafiabackend.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GameExistsValidator.class)
@Documented
public @interface GameExists {

    String message() default "Игры с данным ID не существует";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
