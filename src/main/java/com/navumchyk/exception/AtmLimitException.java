package com.navumchyk.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtmLimitException extends RuntimeException {

    private final String message;

    public AtmLimitException(String message) {

        this.message = message;
    }
}
