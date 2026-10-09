package com.pe.advanced.spring.config;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.context.annotation.Bean;

public class FlywayConfiguration {

    @Bean
    public ClassicConfiguration flywayConfig(HikariDataSource dataSource) {
        final var config = new ClassicConfiguration();
        config.setDataSource(dataSource);
        config.setLocationsAsStrings("classpath:db/migrations");
        config.setConnectRetries(Integer.MAX_VALUE);
        config.setCleanDisabled(true);
        config.setCleanOnValidationError(false);
        config.setOutOfOrder(false);
        config.setValidateOnMigrate(true);
        return config;
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway(ClassicConfiguration flywayConfig) {
        return new Flyway(flywayConfig);
    }
}
