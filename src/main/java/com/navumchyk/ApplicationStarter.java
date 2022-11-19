package com.navumchyk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "com.navumchyk")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ApplicationStarter {

    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(ApplicationStarter.class, args);
    }
}
