package com.ticketing.sys.controller;


import com.ticketing.sys.model.Logs;
import com.ticketing.sys.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/simulator")
public class SimulatorController {


    @Autowired
    SimulationService simulationService;


    @PostMapping("/start")
    public String simulatorStart() {
        simulationService.startTicketing();
        return "Simulating ticket additions...";
    }

    @GetMapping("/stop")
    public ResponseEntity<List<Logs>> getAllLogs() {
        List<Logs> logs = simulationService.stopSimulation();
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }
}
