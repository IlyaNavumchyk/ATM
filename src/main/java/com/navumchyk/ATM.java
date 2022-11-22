package com.navumchyk;

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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
    private boolean isAtmWork = true;

    public void start() {

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
                } while ( !isExitChoice(userChoice));

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
        loadDataFromFileToDatabase();
    }

    @PreDestroy
    private void shutdown() {

        loadDataFromDatabaseToFile();
    }

    private void loadDataFromFileToDatabase() {

        try (Scanner scanner = new Scanner(Path.of(config.getFileName()), StandardCharsets.UTF_8)) {

            while (scanner.hasNext()) {

                String line = scanner.nextLine();
                String[] cardArgs = line.split(" ");
                if (cardArgs.length != 6) {
                    //todo throw new RuntimeException("Card data is corrupted!");
                    continue;
                }

                int balance = Integer.parseInt(cardArgs[2]);
                boolean isCardBlock = Boolean.parseBoolean(cardArgs[3]);
                int countOfInvalidEnteredPinCode = Integer.parseInt(cardArgs[4]);
                LocalDate lastDateInvalidEnteredPinCode = null;
                if (!cardArgs[5].equals("null")) {
                    lastDateInvalidEnteredPinCode = LocalDate.parse(cardArgs[5]);
                }

                Card card = Card.builder()
                        .cardNumber(cardArgs[0])
                        .pinCode(cardArgs[1])
                        .balance(balance)
                        .isCardBlock(isCardBlock)
                        .countOfInvalidlyEnteredPinCode(countOfInvalidEnteredPinCode)
                        .lastDateInvalidlyEnteredPinCode(lastDateInvalidEnteredPinCode)
                        .build();

                database.put(card.getCardNumber(), card);
            }
        } catch (Exception e) {
            //TODO
            log.error(e.getMessage());
        }
    }

    private void loadDataFromDatabaseToFile() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(config.getFileName()))) {

            Card card;
            for (var entry : database.entrySet()) {
                card = entry.getValue();
                writer.write(card.toString() + '\n');
            }
        } catch (Exception e) {
            //TODO
            log.error(e.getMessage());
        }
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
