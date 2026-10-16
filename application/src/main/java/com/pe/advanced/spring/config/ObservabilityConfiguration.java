package com.pe.advanced.spring.config;

import org.springframework.context.annotation.Bean;

import java.io.IOException;

public class ObservabilityConfiguration {

    @Bean
    public BuildInfoContributor buildInfoContributor() throws IOException {
        return new BuildInfoContributor();
    }


}
