package com.navumchyk;

import com.navumchyk.config.ATMConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
@Data
@Slf4j
public class ATM {

    private final ATMConfig config;
    private final Map<String, Account> database = new HashMap<>();

    private int limit;

    @PostConstruct
    private void init() {
        limit = config.getLimit();
        loadDataFromFileToDatabase();
    }

    public void loadDataFromFileToDatabase() {

        try (Scanner scanner = new Scanner(Path.of(config.getFileName()), StandardCharsets.UTF_8)) {

            while (scanner.hasNext()) {

                String line = scanner.nextLine();
                String[] accountArgs = line.split(" ");
                if (accountArgs.length != 6) {
                    throw new RuntimeException("Account data is corrupted!");
                }

                int pinCode = Integer.parseInt(accountArgs[1]);
                int balance = Integer.parseInt(accountArgs[2]);
                boolean isCardBlock = Boolean.parseBoolean(accountArgs[3]);
                int numberOfInvalidEnteredPasswords = Integer.parseInt(accountArgs[4]);
                LocalDateTime lastDateInvalidEnteredPasswords = null;
                if (!accountArgs[5].equals("null")) {
                    lastDateInvalidEnteredPasswords = LocalDateTime.parse(accountArgs[5]);
                }

                Account account = new Account(accountArgs[0], pinCode, balance, isCardBlock,
                        numberOfInvalidEnteredPasswords, lastDateInvalidEnteredPasswords);
                database.put(account.getCardNumber(), account);
            }

        } catch (Exception e) {
            //TODO
            log.error(e.getMessage());
        }
    }

    @PreDestroy
    private void loadDataFromDatabaseToFile() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(config.getFileName()))) {
            StringBuilder sb = new StringBuilder();
            Account account;
            for (var entry : database.entrySet()) {
                account = entry.getValue();
                sb.append(account.getCardNumber()).append(" ");
                sb.append(account.getPinCode()).append(" ");
                sb.append(account.getBalance()).append(" ");
                sb.append(account.isCardBlock()).append(" ");
                sb.append(account.getNumberOfInvalidEnteredPasswords()).append(" ");
                sb.append(account.getLastDateInvalidEnteredPasswords());

                writer.write(sb.toString() + '\n');
                sb.delete(0, sb.length());
            }
        } catch (Exception e) {
            //TODO
            log.error(e.getMessage());
        }
    }
}
