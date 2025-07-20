package com.ticketing.sys.service;

import com.ticketing.sys.model.Logs;

import java.util.List;

public interface SimulationService {
    void startTicketing();

    List<Logs> stopSimulation();
    //   void simulateTickets();
}
