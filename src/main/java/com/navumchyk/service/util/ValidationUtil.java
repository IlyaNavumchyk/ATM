package com.navumchyk.service.util;

public class ValidationUtil {

    private ValidationUtil() {
    }

    private static final String DIGIT_PATTERN = "^\\d{1,9}$";

    public static boolean validateUserInputAgainstNumberPattern(String userChoice) {

        return userChoice.matches(DIGIT_PATTERN);
    }
}
