package com.navumchyk;

public interface ShutDownable {

    String DEFAULT_CHOICE_FOR_SHUT_DOWN_ATM = "exit";

    default boolean isShutDownChoice(String userChoice) {

        return DEFAULT_CHOICE_FOR_SHUT_DOWN_ATM.equalsIgnoreCase(userChoice);
    }
}
