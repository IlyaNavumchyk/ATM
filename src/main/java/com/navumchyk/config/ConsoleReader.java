package com.navumchyk.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.Scanner;

@Configuration
@RequiredArgsConstructor
public class ConsoleReader {

    private final Scanner scanner;

    public String nextLine() {

        return scanner.nextLine();
    }

    @PreDestroy
    public void close() {

        scanner.close();
    }
}
