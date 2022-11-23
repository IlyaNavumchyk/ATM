package com.navumchyk.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataLoadingError extends RuntimeException {

    private final String message;

    public DataLoadingError(String message) {

        this.message = message;
    }
}
