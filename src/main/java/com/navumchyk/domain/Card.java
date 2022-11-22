package com.navumchyk.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Card {

    //card credentials
    private final String cardNumber;
    private final String pinCode;

    //card details
    private int balance;
    private boolean isCardBlock;
    private int countOfInvalidlyEnteredPinCode;
    private LocalDate lastDateInvalidlyEnteredPinCode;

    @Override
    public String toString() {

        return cardNumber + " " +
                pinCode + " " +
                balance + " " +
                isCardBlock + " " +
                countOfInvalidlyEnteredPinCode + " " +
                lastDateInvalidlyEnteredPinCode;
    }
}
