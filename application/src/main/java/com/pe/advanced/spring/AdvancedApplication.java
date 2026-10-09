package com.pe.advanced.spring;

import com.pe.advanced.spring.config.FlywayConfiguration;
import com.pe.advanced.spring.config.PersistenceConfiguration;
import com.pe.advanced.spring.config.ServiceWorkersConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "com.pe.advanced.spring.*"
))
@Import({
        FlywayConfiguration.class,
        PersistenceConfiguration.class,
        ServiceWorkersConfiguration.class
})
public class AdvancedApplication {

    static void main(String[] args) {
        SpringApplication.run(AdvancedApplication.class, args);
    }
}