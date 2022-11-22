package com.navumchyk.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidlyCardDataException extends RuntimeException {

    private final String message;

    public InvalidlyCardDataException(String message) {

        this.message = message;
    }
}
