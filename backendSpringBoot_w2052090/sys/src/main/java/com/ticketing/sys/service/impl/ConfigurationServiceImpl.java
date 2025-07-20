package com.ticketing.sys.service.impl;

import com.ticketing.sys.dto.AddConfigurationDto;
import com.ticketing.sys.model.Configuration;
import com.ticketing.sys.repository.ConfigurationRepository;
import com.ticketing.sys.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    ConfigurationRepository configurationRepository;
    @Override
    public String saveConfiguration(AddConfigurationDto addConfigurationDto) {
        Configuration configuration = new Configuration();
        configuration.setCustomerRetrievalRate(addConfigurationDto.getCustomerRetrievalRate());
        configuration.setMaxTicketCapacity(addConfigurationDto.getMaxTicketCapacity());
        configuration.setNumberOfCustomers(addConfigurationDto.getNumberOfCustomers());
        configuration.setTotalTickets(addConfigurationDto.getTotalTickets());
        configuration.setNumberOfVendors(addConfigurationDto.getNumberOfVendors());
        configuration.setTicketReleaseRate(addConfigurationDto.getTicketReleaseRate());
        if (configurationRepository.save(configuration)!=null) {
            return "configuration saved successfully";
        }
        else {
            return "error while saving configuration";
        }
    }
}
