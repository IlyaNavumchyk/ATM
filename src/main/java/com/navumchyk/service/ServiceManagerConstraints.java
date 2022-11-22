package com.navumchyk.service;

public interface ServiceManagerConstraints {

    int TOP_UP_LIMIT = 1_000_000;

    default boolean checkConditionsBeforeTopUp(int userMoneyAmount) {

        return userMoneyAmount <= TOP_UP_LIMIT;
    }
}
