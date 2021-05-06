package com.mafia.mafiabackend.config;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<CustomError> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<CustomError> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.add(CustomError.builder()
                    .errorMessage(error.getDefaultMessage())
                    .fieldName(((FieldError) error).getField())
                    .rejectedValue(((FieldError) error).getRejectedValue())
                    .build());
        });
        return errors;
    }

    @Data
    @Builder
    private static class CustomError {
        private String errorMessage;
        private Object rejectedValue;
        private String fieldName;
    }


}
