package com.ticketing.sys.controller;

import com.ticketing.sys.service.ConfigurationService;
import com.ticketing.sys.dto.AddConfigurationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/configurations")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @PostMapping("/add")
    public String addConfiguration(@RequestBody AddConfigurationDto addConfigurationDto) {
        return configurationService.saveConfiguration(addConfigurationDto);
    }
}
