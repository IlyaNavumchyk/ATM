package com.navumchyk.atm.util;

import com.navumchyk.domain.Card;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;

import static com.navumchyk.atm.util.CardParser.parseInputToCard;

@Slf4j
public class DatabaseLoader {

    private DatabaseLoader() {
    }

    public static void loadDataFromFileToDatabase(Map<String, Card> database, String fileName) {

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

    public static void loadDataFromDatabaseToFile(Map<String, Card> database, String fileName) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            Card card;
            for (var entry : database.entrySet()) {

                card = entry.getValue();
                writer.write(card.toString() + '\n');
            }
        } catch (IOException e) {
            log.error("Database not uploaded: " + e.getMessage());
        }
    }
}
