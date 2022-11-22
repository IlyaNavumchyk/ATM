package com.navumchyk.security;

import com.navumchyk.domain.Card;

public interface SecurityManagerOperations {

    String CARD_NUMBER_FORMAT = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}$";
    String PIN_CODE_FORMAT = "^\\d{4}$";
    int COUNT_OF_INVALIDLY_ENTERED_PIN_CODE = 3;

    default boolean checkCardNumberFormat(String cardNumber) {

        return cardNumber.matches(CARD_NUMBER_FORMAT);
    }

    default boolean checkPinCodeFormat(String pinCode) {

        return pinCode.matches(PIN_CODE_FORMAT);
    }

    default boolean checkCardForInvalidlyEnteredPinCode(Card card) {

        return card.getCountOfInvalidlyEnteredPinCode() == COUNT_OF_INVALIDLY_ENTERED_PIN_CODE;
    }
}
