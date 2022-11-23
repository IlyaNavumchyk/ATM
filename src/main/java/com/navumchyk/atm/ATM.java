package com.navumchyk.atm;

import com.navumchyk.Exitable;
import com.navumchyk.config.ATMConfig;
import com.navumchyk.domain.Card;
import com.navumchyk.exception.InvalidlyCardDataException;
import com.navumchyk.security.SecurityManager;
import com.navumchyk.service.ServiceManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

import static com.navumchyk.atm.util.DatabaseLoader.loadDataFromDatabaseToFile;
import static com.navumchyk.atm.util.DatabaseLoader.loadDataFromFileToDatabase;
import static com.navumchyk.util.DelayUtil.makeDelayOfTwoSeconds;

@Component
@Data
@Slf4j
public class ATM implements Exitable {

    private final ATMConfig config;
    private final Map<String, Card> database = new HashMap<>();

    private final SecurityManager securityManager;
    private final ServiceManager serviceManager;

    private int atmBalance;
    private boolean isAtmWork;

    public void start() {

        isAtmWork = true;
        log.warn("ATM has started working!");

        Card card;
        String userChoice;

        while (isAtmWork) {

            try {

                card = securityManager.findCardByNumber(database);

                if (card == null) {
                    getFinishMessage();
                    continue;
                }

                securityManager.checkCardForBlocking(card);

                if (!securityManager.authenticateCardByPinCode(database)) {
                    continue;
                }

                do {
                    userChoice = serviceManager.showMenuAndReturnUserChoice();

                    atmBalance = serviceManager.executeServiceOperations(userChoice, card, atmBalance);
                } while (!isExitChoice(userChoice));

            } catch (InvalidlyCardDataException e) {
                log.error(e.getMessage());
                makeDelayOfTwoSeconds();
            } catch (Exception e) {
                log.error(getErrorMessage(e));
            }

            getFinishMessage();
        }
    }

    @PostConstruct
    private void init() {
        atmBalance = config.getBalance();
        loadDataFromFileToDatabase(database, config.getFileName());
    }

    @PreDestroy
    private void shutdown() {

        loadDataFromDatabaseToFile(database, config.getFileName());
        log.warn("Database uploaded successfully!");
    }

    private void getFinishMessage() {
        log.info("Take your card!!!");
        makeDelayOfTwoSeconds();
    }

    private String getErrorMessage(Exception e) {

        return String.format(
                "Oops something went wrong. %s with message %s.",
                e.getClass().getSimpleName(),
                e.getMessage());
    }
}
