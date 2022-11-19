package com.navumchyk.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("atm")
@Getter
@Setter
public class ATMConfig {

    private int limit;
    private String fileName;
}
