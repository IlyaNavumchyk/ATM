package com.navumchyk.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class DelayUtil {

    private DelayUtil() {
    }

    public static void makeDelayOfTwoSeconds() {

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
