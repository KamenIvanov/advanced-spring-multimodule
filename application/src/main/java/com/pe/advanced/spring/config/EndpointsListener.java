package com.pe.advanced.spring.config;

import com.pe.advanced.domain.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
        final List<Pair<String, String>> endpoints = new ArrayList<>();

        logger.info("===========================Rest Endpoints===========================");
        final var applicationContext = event.getApplicationContext();
        applicationContext.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class).getHandlerMethods()
                .forEach((endpoint, _) -> {
                    if (endpoint.getPathPatternsCondition() != null) {
                        endpoints.add(new Pair<>(endpoint.getMethodsCondition().toString(), endpoint.getPathPatternsCondition().toString()));
                    }
                });

        endpoints
                .stream().sorted(Comparator.comparing(Pair::second))
                .forEach(pair -> logger.info("{}: {}", pair.first(), pair.second()));
    }
}
