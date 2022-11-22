package com.navumchyk.service;

import com.navumchyk.domain.Card;

public interface ServiceManager extends ServiceManagerOperations, ServiceManagerConstraints{

    String showMenuAndReturnUserChoice();

    int executeServiceOperations(String userChoice, Card card, int atmBalance);
}
