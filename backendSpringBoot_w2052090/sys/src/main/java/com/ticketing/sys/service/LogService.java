package com.ticketing.sys.service;

import org.springframework.scheduling.annotation.Async;

public interface LogService {
    @Async
    void logMessage(String message);
}
