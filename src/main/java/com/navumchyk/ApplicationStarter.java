package com.navumchyk;

import com.navumchyk.atm.ATM;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "com.navumchyk")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ApplicationStarter {

    public static void main(String[] args) {

        ApplicationContext applicationContext = SpringApplication.run(ApplicationStarter.class, args);

        ATM atm = applicationContext.getBean(ATM.class);

        atm.start();
    }
}
