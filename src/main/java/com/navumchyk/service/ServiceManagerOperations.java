package com.navumchyk.service;

import com.navumchyk.domain.Card;

public interface ServiceManagerOperations {

    String DEFAULT_CHOICE_FOR_CHECK_BALANCE = "1";
    String DEFAULT_CHOICE_FOR_GET_MONEY_FROM_BALANCE = "2";
    String DEFAULT_CHOICE_FOR_TOP_UP_BALANCE = "3";

    void checkBalance(Card card);

    int withdrawFromBalance(Card card, int atmBalance);

    void topUpBalance(Card card);
}
