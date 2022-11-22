package com.navumchyk.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Configuration
@ConfigurationProperties("atm")
@Getter
@Setter
public class ATMConfig {

    private int balance;
    private String fileName;

    @Bean
    public Scanner getScanner() {

        return new Scanner(System.in, StandardCharsets.UTF_8);
    }
}
