package com.pe.advanced.spring.config;

import org.springframework.boot.actuate.info.MapInfoContributor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class BuildInfoContributor extends MapInfoContributor {

    private static final String BUILD_PROPERTIES_FILE = "/build.properties";

    public BuildInfoContributor() throws IOException {
        super(readBuildProperties());
    }

    private static Map<String, Object> readBuildProperties() throws IOException {
        try (final InputStream is = BuildInfoContributor.class.getResourceAsStream(BUILD_PROPERTIES_FILE)) {
            final var productInfoProperties = new Properties();
            productInfoProperties.load(is);

            final var buildProperties = new TreeMap<String, Object>();
            for (final Map.Entry<Object, Object> buildProperty : productInfoProperties.entrySet()) {
                buildProperties.put(buildProperty.getKey().toString(), buildProperty.getValue());
            }
            return buildProperties;
        }
    }
}
