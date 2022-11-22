package com.navumchyk;

public interface Exitable {

    String DEFAULT_CHOICE_FOR_EXIT = "0";

    default boolean isExitChoice(String userChoice) {

        return DEFAULT_CHOICE_FOR_EXIT.equals(userChoice);
    }
}
