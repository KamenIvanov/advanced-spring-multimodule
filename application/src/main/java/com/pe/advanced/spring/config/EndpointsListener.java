package com.pe.advanced.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Logs the REST endpoints once the application is started.
 * <p>
 * Listens for {@link ApplicationReadyEvent}, not {@code ContextRefreshedEvent}: the latter is fired for every child
 * context too (e.g. the actuator one on a separate {@code management.server.port}) and reaches the listeners of the
 * parent, so the endpoints were logged once per context.
 *
 * @author kamen on 1.06.22 г.
 */
public class EndpointsListener implements ApplicationListener<ApplicationReadyEvent> {

    public static final  EndpointsListener instance = new EndpointsListener();
    private static final Logger logger   = LoggerFactory.getLogger(EndpointsListener.class);

    private EndpointsListener() {
        // Singleton
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        final var mappings = event.getApplicationContext().getBean(RequestMappingHandlerMapping.class).getHandlerMethods().keySet();

        final var endpoints = mappings.stream()
                .filter(info -> info.getPathPatternsCondition() != null)
                .map(info -> new Endpoint(
                        String.join(",", info.getPathPatternsCondition().getPatternValues()),
                        info.getMethodsCondition().getMethods().stream().map(Enum::name).sorted().collect(Collectors.joining(","))))
                .sorted(Comparator.comparing(Endpoint::path).thenComparing(Endpoint::methods))
                .toList();

        logger.info("=========================== {} rest endpoints ===========================", endpoints.size());
        endpoints.forEach(endpoint -> logger.info("{} {}", endpoint.methods(), endpoint.path()));

        final var withoutPath = mappings.size() - endpoints.size();
        if (withoutPath > 0) {
            logger.warn("{} mappings have no path pattern and are not listed", withoutPath);
        }
    }

    /**
     * One line of the listing. Local to the listener because it exists to be sorted and printed,
     * and nothing outside this class has any use for it.
     */
    private record Endpoint(String path, String methods) {
    }
}
