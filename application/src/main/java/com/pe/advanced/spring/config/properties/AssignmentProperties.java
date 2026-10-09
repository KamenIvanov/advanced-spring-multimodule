package com.pe.advanced.spring.config.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.assignments")
@Validated
public record AssignmentProperties(
        @NotNull @Min(1) @Max(1000) Integer maxBatchSize
) {
}