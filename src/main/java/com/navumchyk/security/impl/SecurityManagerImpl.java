package com.navumchyk.security.impl;

import com.navumchyk.Exitable;
import com.navumchyk.ShutDownable;
import com.navumchyk.config.ConsoleReader;
import com.navumchyk.domain.Card;
import com.navumchyk.exception.InvalidlyCardDataException;
import com.navumchyk.exception.ShutDownException;
import com.navumchyk.security.SecurityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

import static com.navumchyk.util.DelayUtil.makeDelayOfTwoSeconds;
import static com.navumchyk.util.LoggerUtil.showWarnMessageAndMakeDelay;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityManagerImpl implements SecurityManager, Exitable, ShutDownable {

    private final ConsoleReader reader;

    private Card card;

    private boolean isCardAuthenticate;
    private boolean isInputCorrect;
    private String cardNumber;
    private String pinCode;

    public Card findCardByNumber(Map<String, Card> database) {

        card = null;
        isInputCorrect = false;

        while (!isInputCorrect) {

            log.info("Please enter card number in format \"dddd-dddd-dddd-dddd\" or 0 to exit:");
            cardNumber = reader.nextLine();

            if (isExitChoice(cardNumber)) {
                break;
            }

            if (isShutDownChoice(cardNumber)) {
                throw new ShutDownException();
            }

            if (checkCardNumberFormat(cardNumber)) {
                isInputCorrect = true;
            } else {
                showWarnMessageAndMakeDelay("Invalid card number format.");
            }
        }

        if (!isInputCorrect) {
            return card;
        }

        if (database.containsKey(cardNumber)) {
            card = database.get(cardNumber);
        } else {
            throw new InvalidlyCardDataException(
                    String.format("Card with this number %s is not registered.", cardNumber));
        }

        return card;
    }

    public void checkCardForBlocking(Card card) {

        if (card.getLastDateInvalidlyEnteredPinCode() != null &&
                card.getLastDateInvalidlyEnteredPinCode().isBefore(LocalDate.now())) {

            card.setCountOfInvalidlyEnteredPinCode(0);
            card.setCardBlock(false);
        }

        isCardBlockThenThrow();
    }

    public boolean authenticateCardByPinCode(Map<String, Card> database) {

        isCardAuthenticate = false;

        while (!isCardAuthenticate) {

            if (card.isCardBlock()) {
                break;
            }

            isInputCorrect = false;

            while (!isInputCorrect) {

                log.info("Please enter pin code in format \"dddd\" or 0 to exit:");
                pinCode = reader.nextLine();

                if (isExitChoice(pinCode)) {

                    break;
                }

                if (checkPinCodeFormat(pinCode)) {
                    isInputCorrect = true;
                } else {
                    showWarnMessageAndMakeDelay("Invalid pin code format.");
                }
            }

            if (!isInputCorrect) {
                return isCardAuthenticate;
            }

            try {
                if (card.getPinCode().equals(pinCode)) {
                    isCardAuthenticate = true;
                } else {
                    executeWhenPinCodeIsEnteredIncorrectly(card);
                }

            } catch (InvalidlyCardDataException e) {
                log.error(e.getMessage());
                makeDelayOfTwoSeconds();
            }
        }

        return isCardAuthenticate;
    }

    private void executeWhenPinCodeIsEnteredIncorrectly(Card card) throws InvalidlyCardDataException {

        int countOfInvalidlyEnteredPinCode = card.getCountOfInvalidlyEnteredPinCode() + 1;

        card.setCountOfInvalidlyEnteredPinCode(countOfInvalidlyEnteredPinCode);
        card.setLastDateInvalidlyEnteredPinCode(LocalDate.now());

        if (checkCardForInvalidlyEnteredPinCode(card)) {
            card.setCardBlock(true);
            throw new InvalidlyCardDataException(String.format(
                    "PIN code was entered invalidly 3 times. Card %s is blocked.",
                    card.getCardNumber()));
        } else {
            throw new InvalidlyCardDataException(String.format(
                    "PIN code was entered invalidly %d times. There are %d attempts left.",
                    countOfInvalidlyEnteredPinCode,
                    COUNT_OF_INVALIDLY_ENTERED_PIN_CODE - countOfInvalidlyEnteredPinCode));
        }
    }

    private void isCardBlockThenThrow() throws InvalidlyCardDataException {

        if (card.isCardBlock()) {
            throw new InvalidlyCardDataException(
                    String.format("Card %s is blocked.", card.getCardNumber()));
        }
    }
}
