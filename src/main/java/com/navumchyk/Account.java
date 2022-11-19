package com.navumchyk;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Account {

    //card credentials
    private final String cardNumber;
    private final int pinCode;

    //account details
    private int balance;
    private boolean isCardBlock;
    private int numberOfInvalidEnteredPasswords;
    private LocalDateTime lastDateInvalidEnteredPasswords;

}
