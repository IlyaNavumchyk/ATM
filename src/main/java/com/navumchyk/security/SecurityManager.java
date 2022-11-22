package com.navumchyk.security;

import com.navumchyk.domain.Card;
import com.navumchyk.exception.AtmLimitException;

import java.util.Map;

public interface SecurityManager extends SecurityManagerOperations {

    Card findCardByNumber(Map<String, Card> database) throws AtmLimitException;

    void checkCardForBlocking(Card card) throws AtmLimitException;

    boolean authenticateCardByPinCode(Map<String, Card> database) throws AtmLimitException;
}
