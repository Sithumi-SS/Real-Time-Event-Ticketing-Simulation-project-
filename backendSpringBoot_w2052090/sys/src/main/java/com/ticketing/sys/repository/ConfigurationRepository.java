package com.ticketing.sys.repository;


import com.ticketing.sys.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    @Query("SELECT c FROM Configuration c ORDER BY c.configId DESC")
    List<Configuration> findTopByOrderByConfigIdDesc();
}
