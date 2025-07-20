package com.ticketing.sys.service.impl;

import com.ticketing.sys.model.Configuration;
import com.ticketing.sys.model.Logs;
import com.ticketing.sys.repository.ConfigurationRepository;
import com.ticketing.sys.repository.LogsRepository;
import com.ticketing.sys.service.LogService;
import com.ticketing.sys.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.RuntimeErrorException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SimulationServiceImpl implements SimulationService {
    @Autowired
    ConfigurationRepository configurationRepository;


    @Autowired
    LogsRepository logsRepository;
    @Autowired
    LogService logService;
    private final AtomicInteger ticketCounter = new AtomicInteger(0);
    private ExecutorService vendorExecutorService;
    private ExecutorService customerExecutorService;
    private volatile boolean running = false;


    @Override
    public synchronized void startTicketing() {
        Configuration config;
        if (running) {
            throw new IllegalStateException("Simulation is already running.");
        }

        List<Configuration> list = configurationRepository.findTopByOrderByConfigIdDesc();

        if (list!=null){
            config = list.get(0);
        }
        else{
            throw new RuntimeException("Configuration not founded");
        }
        ticketCounter.set(config.getTotalTickets());
        vendorExecutorService = Executors.newFixedThreadPool(config.getNumberOfVendors());
        customerExecutorService = Executors.newFixedThreadPool(config.getNumberOfCustomers());
        running = true;

        // Start vendors adding tickets
        for (int i = 0; i < config.getNumberOfVendors(); i++) {
            int vendorId = i + 1;
            vendorExecutorService.submit(() -> addTicketsPeriodically(config, vendorId));
        }

        // Start customers purchasing tickets
        for (int i = 0; i < config.getNumberOfCustomers(); i++) {
            int customerId = i + 1;
            customerExecutorService.submit(() -> purchaseTicketsPeriodically(config, customerId));
        }
    }

    private void addTicketsPeriodically(Configuration config, int vendorId) {
        try {
            while (running) {
                synchronized (ticketCounter) {
                    if (ticketCounter.get() < config.getMaxTicketCapacity()) {
                        ticketCounter.incrementAndGet();
                        logService.logMessage("Vendor " + vendorId + " added a ticket. Current total: " + ticketCounter.get());
                    } else {
                        logService.logMessage("Vendor " + vendorId + " waiting as max capacity is reached.");
                    }
                }
                Thread.sleep(config.getTicketReleaseRate());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logService.logMessage("Vendor " + vendorId + " interrupted.");
        }
    }

    private void purchaseTicketsPeriodically(Configuration config, int customerId) {
        try {
            while (running) {
                synchronized (ticketCounter) {
                    if (ticketCounter.get() > 0) {
                        ticketCounter.decrementAndGet();
                        logService.logMessage("Customer " + customerId + " purchased a ticket. Current total: " + ticketCounter.get());
                    } else {
                        logService.logMessage("Customer " + customerId + " waiting as no tickets are available.");
                    }
                }
                Thread.sleep(config.getCustomerRetrievalRate());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logService.logMessage("Customer " + customerId + " interrupted.");
        }
    }


    @Override
    public synchronized List<Logs> stopSimulation() {
        running = false;
        if (vendorExecutorService != null) {
            vendorExecutorService.shutdownNow();
        }
        if (customerExecutorService != null) {
            customerExecutorService.shutdownNow();
        }
        logService.logMessage("Simulation stopped.");
        return logsRepository.findAll();
    }

}
