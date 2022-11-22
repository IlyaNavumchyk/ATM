package com.navumchyk.util;

import lombok.extern.slf4j.Slf4j;

import static com.navumchyk.util.DelayUtil.makeDelayOfTwoSeconds;

@Slf4j
public class LoggerUtil {

    private LoggerUtil() {
    }

    public static void showWarnMessageAndMakeDelay(String warnMessage) {

        log.warn(warnMessage);
        makeDelayOfTwoSeconds();
    }
}
