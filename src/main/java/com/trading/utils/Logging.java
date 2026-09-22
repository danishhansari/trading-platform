package com.trading.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Logging {
    public void logForUser(Long userId, String level, String messageTemplate, Object... args) {
        String previous = MDC.get("userId");
        try {
            MDC.put("userId", "trader-" + userId);
            switch (level) {
                case "WARN" -> log.warn(messageTemplate, args);
                default -> log.info(messageTemplate, args);
            }
        } finally {
            if (previous != null) {
                MDC.put("userId", previous);
            } else {
                MDC.remove("userId");
            }
        }
    }
}
