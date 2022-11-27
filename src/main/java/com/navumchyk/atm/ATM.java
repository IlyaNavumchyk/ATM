package com.navumchyk.atm;

import com.navumchyk.Exitable;
import com.navumchyk.config.ATMConfig;
import com.navumchyk.database.DatabaseManager;
import com.navumchyk.domain.Card;
import com.navumchyk.exception.InvalidlyCardDataException;
import com.navumchyk.exception.ShutDownException;
import com.navumchyk.security.SecurityManager;
import com.navumchyk.service.ServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.navumchyk.util.DelayUtil.makeDelayOfTwoSeconds;

@Component
@Slf4j
public class ATM implements Exitable {

    private final DatabaseManager databaseManager;
    private final SecurityManager securityManager;
    private final ServiceManager serviceManager;

    private int atmBalance;
    private boolean isAtmWork;

    @Autowired
    private ATM(final DatabaseManager databaseManager, final SecurityManager securityManager,
                final ServiceManager serviceManager, final ATMConfig config) {

        this.databaseManager = databaseManager;
        this.securityManager = securityManager;
        this.serviceManager = serviceManager;
        this.atmBalance = config.getBalance();
        this.isAtmWork = true;
    }

    public void start() {

        log.warn("ATM has started working!");

        var database = databaseManager.getDatabase();

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
                    getFinishMessage();
                    continue;
                }

                do {
                    userChoice = serviceManager.showMenuAndReturnUserChoice();

                    atmBalance = serviceManager.executeServiceOperations(userChoice, card, atmBalance);
                } while (!isExitChoice(userChoice));

            } catch (InvalidlyCardDataException e) {
                log.error(e.getMessage());
                makeDelayOfTwoSeconds();
            } catch (ShutDownException e) {
                log.warn("ATM shutdown selected!");
                isAtmWork = false;
            } catch (Exception e) {
                log.error(getErrorMessage(e));
            }

            getFinishMessage();
        }
    }

    @PreDestroy
    private void shutdown() {

        log.warn("ATM has finished working!");
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
