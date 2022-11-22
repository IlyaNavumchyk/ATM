package com.navumchyk.service.impl;

import com.navumchyk.Exitable;
import com.navumchyk.config.ConsoleReader;
import com.navumchyk.domain.Card;
import com.navumchyk.exception.AtmLimitException;
import com.navumchyk.exception.InvalidlyCardDataException;
import com.navumchyk.service.ServiceManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.navumchyk.service.util.ValidationUtil.validateUserInputAgainstNumberPattern;
import static com.navumchyk.util.DelayUtil.makeDelayOfTwoSeconds;
import static com.navumchyk.util.LoggerUtil.showWarnMessageAndMakeDelay;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceImpl implements ServiceManager, Exitable {

    private final ConsoleReader reader;

    boolean isInputCorrect;
    String userInput;
    int userMoneyAmount;

    public String showMenuAndReturnUserChoice() {

        log.info(getUserMenu());
        return reader.nextLine();
    }

    public int executeServiceOperations(String userChoice, Card card, int atmBalance) {

        try {
            switch (userChoice) {
                case DEFAULT_CHOICE_FOR_EXIT: {
                    break;
                }
                case DEFAULT_CHOICE_FOR_CHECK_BALANCE: {
                    checkBalance(card);
                    break;
                }
                case DEFAULT_CHOICE_FOR_GET_MONEY_FROM_BALANCE: {
                    atmBalance = withdrawFromBalance(card, atmBalance);
                    break;
                }
                case DEFAULT_CHOICE_FOR_TOP_UP_BALANCE: {
                    topUpBalance(card);
                    break;
                }
                default: {
                    showWarnMessage();
                }
            }

            return atmBalance;

        } catch (InvalidlyCardDataException | AtmLimitException e) {
            log.error(e.getMessage());
            makeDelayOfTwoSeconds();
            return atmBalance;
        }
    }

    private String getUserMenu() {

        return "Select operation number:\n" +
                "1. Check balance\n" +
                "2. Withdraw from balance\n" +
                "3. Top up balance\n" +
                "\n0. Exit";
    }

    @Override
    public void checkBalance(Card card) {

        log.info(String.format("Your balance is %d", card.getBalance()));
        makeDelayOfTwoSeconds();
    }

    @Override
    public int withdrawFromBalance(Card card, int atmBalance) {

        getUserMoneyAmount();

        if (!isInputCorrect) {
            return atmBalance;
        }

        checkUserInputForNonNegative();

        checkConditionsBeforeWithdraw(card, atmBalance);

        card.setBalance(card.getBalance() - userMoneyAmount);
        atmBalance = atmBalance - userMoneyAmount;

        return atmBalance;
    }

    @Override
    public void topUpBalance(Card card) {

        getUserMoneyAmount();

        checkUserInputForNonNegative();

        if (checkConditionsBeforeTopUp(userMoneyAmount)) {
            card.setBalance(card.getBalance() + userMoneyAmount);
        } else {
            throw new AtmLimitException(
                    String.format("%d limit exceeded.", TOP_UP_LIMIT));
        }
    }

    private void getUserMoneyAmount() {

        isInputCorrect = false;
        userMoneyAmount = 0;

        while (!isInputCorrect) {

            log.info("Enter amount or 0 to exit:");
            userInput = reader.nextLine();

            if (isExitChoice(userInput)) {
                break;
            }

            if (validateUserInputAgainstNumberPattern(userInput)) {
                userMoneyAmount = Integer.parseInt(userInput);
                isInputCorrect = true;
            } else {
                showWarnMessage();
            }
        }
    }

    private void checkUserInputForNonNegative() {

        if (userMoneyAmount < 0) {
            showWarnMessage();
        }
    }

    private void checkConditionsBeforeWithdraw(Card card, int atmBalance) {

        if (card.getBalance() < userMoneyAmount) {
            throw new InvalidlyCardDataException("Not enough money on the card.");
        }

        if (atmBalance < userMoneyAmount) {
            throw new AtmLimitException("Not enough money in the ATM");
        }
    }

    private void showWarnMessage() {

        showWarnMessageAndMakeDelay("Incorrect input.");
    }
}
