package com.mafia.mafiabackend.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PlayerExistsValidator.class)
@Documented
public @interface PlayerExists {

    String message() default "Пользователя с данным ID не существует";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
