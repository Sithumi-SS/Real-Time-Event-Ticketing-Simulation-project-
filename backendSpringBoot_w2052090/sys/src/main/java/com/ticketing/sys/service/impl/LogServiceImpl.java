package com.ticketing.sys.service.impl;

import com.ticketing.sys.model.Logs;
import com.ticketing.sys.repository.LogsRepository;
import com.ticketing.sys.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    LogsRepository logsRepository;

    @Async
    @Override
    public void logMessage(String message) {
        Logs logEntry = new Logs();
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setLog(message);
        logsRepository.save(logEntry); // Save log to the database
        System.out.println(LocalDateTime.now() + " - " + message); // Print log to the console
    }
}
