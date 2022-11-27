package com.navumchyk.database.util;

import com.navumchyk.domain.Card;
import com.navumchyk.exception.DataLoadingError;
import com.navumchyk.security.SecurityManagerOperations;

import java.time.LocalDate;

public class CardParser implements SecurityManagerOperations {

    private static final String BALANCE_PATTERN = "^-?\\d{1,9}$";

    private CardParser() {
    }

    public static Card parseInputToCard(String input) throws DataLoadingError {

        String[] cardArgs = input.split(" ");

        if (cardArgs.length != 6) {
            throw new DataLoadingError("Card data args < 6");
        }

        if (!cardArgs[0].matches(CARD_NUMBER_FORMAT)) {
            throw new DataLoadingError("Card number does not match the format" +
                    "\"dddd-dddd-dddd-dddd\"");
        }

        if (!cardArgs[1].matches(PIN_CODE_FORMAT)) {
            throw new DataLoadingError("Pin cod does not match the format \"dddd\"");
        }

        if (!cardArgs[2].matches(BALANCE_PATTERN)) {
            throw new DataLoadingError("Amount not allowed");
        }

        if (!("false".equals(cardArgs[3])) && !("true".equalsIgnoreCase(cardArgs[3]))) {

            throw new DataLoadingError("User blocking flag does not equals true or false");
        }

        int countOfInvalidEnteredPinCode = Integer.parseInt(cardArgs[4]);
        if (countOfInvalidEnteredPinCode > COUNT_OF_INVALIDLY_ENTERED_PIN_CODE) {
            throw new DataLoadingError("Invalid number of wrong entered pin code");
        }

        LocalDate lastDateInvalidEnteredPinCode = null;
        if (!cardArgs[5].equals("null")) {
            lastDateInvalidEnteredPinCode = LocalDate.parse(cardArgs[5]);
        }

        return Card.builder()
                .cardNumber(cardArgs[0])
                .pinCode(cardArgs[1])
                .balance(Integer.parseInt(cardArgs[2]))
                .isCardBlock(Boolean.parseBoolean(cardArgs[3]))
                .countOfInvalidlyEnteredPinCode(countOfInvalidEnteredPinCode)
                .lastDateInvalidlyEnteredPinCode(lastDateInvalidEnteredPinCode)
                .build();
    }
}
