package com.navumchyk.database;

import com.navumchyk.config.ATMConfig;
import com.navumchyk.domain.Card;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.navumchyk.database.util.CardParser.parseInputToCard;

@Slf4j
@Component
public class DatabaseManager {

    private final Map<String, Card> database = new HashMap<>();

    private final String fileName;

    @Autowired
    public DatabaseManager(ATMConfig config) {
        this.fileName = config.getFileName();
    }

    public Map<String, Card> getDatabase() {

        return database;
    }

    @PostConstruct
    private void loadDataFromFileToDatabase() {

        try (Scanner scanner = new Scanner(Path.of(fileName), StandardCharsets.UTF_8)) {

            Card card;
            String input = null;

            while (scanner.hasNext()) {

                try {

                    input = scanner.nextLine();
                    card = parseInputToCard(input);
                    database.put(card.getCardNumber(), card);

                } catch (Exception e) {
                    log.error(String.format(
                            "Card data is corrupted! Error message: %s. Corrupted data: %s",
                            e.getMessage(),
                            input));
                }
            }

            log.warn("Database downloaded successfully!");

        } catch (IOException | InvalidPathException e) {
            log.error("Database not downloaded: " + e.getMessage());
        }
    }

    @PreDestroy
    private void loadDataFromDatabaseToFile() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            Card card;

            for (var entry : database.entrySet()) {

                card = entry.getValue();
                writer.write(card.toString() + '\n');
            }

            log.warn("Database uploaded successfully!");

        } catch (IOException e) {
            log.error("Database not uploaded: " + e.getMessage());
        }
    }
}
