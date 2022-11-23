package com.navumchyk.service;

public interface ServiceManagerConstraints {

    int TOP_UP_LIMIT = 1_000_000;
    String MONEY_LIMIT_PATTERN = "^\\d{1,9}$";

    default boolean checkConditionsBeforeTopUp(int userMoneyAmount) {

        return userMoneyAmount <= TOP_UP_LIMIT;
    }

    default boolean validateUserInputAgainstNumberPattern(String userChoice) {

        return userChoice.matches(MONEY_LIMIT_PATTERN);
    }
}
