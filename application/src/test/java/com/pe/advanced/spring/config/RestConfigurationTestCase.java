package com.pe.advanced.spring.config;

import com.pe.advanced.api.rest.pub.product.ProductsFilteringRestService;
import com.pe.advanced.api.rest.secured.SecuredRestService;
import com.pe.advanced.api.rest.secured.product.ProductsRestService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Nothing in this application is component scanned, so every controller reaches the context
 * because somebody wrote a line for it. A forgotten line removes a resource from the API, the
 * application still starts, and nothing says so - the same silent absence the generated
 * specification can have, from the other side of the contract.
 * <p>
 * The endpoint listener prints the surface at startup, but printing requires somebody to read it.
 * This fails instead.
 */
class RestConfigurationTestCase {

    private static final List<Class<?>> CONTRACTS = List.of(
            ProductsRestService.class,
            ProductsFilteringRestService.class
    );

    @Test
    void declaresABeanForEveryContract() {
        final var declared = beanTypes();

        for (var contract : CONTRACTS) {
            assertTrue(
                    declared.stream().anyMatch(contract::isAssignableFrom),
                    "No bean is declared for the contract " + contract.getSimpleName() + ", so its operations are absent from a running application"
            );
        }
    }

    /**
     * The mapping annotations are enough for the handler mapping to find a bean, but not for its
     * return value to be written as the response body. Without this annotation the endpoint
     * exists and answers wrongly, which is worse than not existing.
     */
    @Test
    void annotatesEveryControllerAsARestController() {
        for (var beanType : beanTypes()) {
            if (CONTRACTS.stream().noneMatch(contract -> contract.isAssignableFrom(beanType))) {
                continue;
            }

            assertTrue(
                    AnnotatedElementUtils.hasAnnotation(beanType, RestController.class),
                    beanType.getSimpleName() + " must be a @RestController, or its responses become view names"
            );
        }
    }

    /**
     * The anonymous resource must not be able to reach the current requester. A resource that can
     * is a resource somebody eventually makes depend on one.
     */
    @Test
    void keepsTheAnonymousContractOutOfTheSecuredHierarchy() {
        assertFalse(SecuredRestService.class.isAssignableFrom(ProductsFilteringRestService.class));
        assertTrue(SecuredRestService.class.isAssignableFrom(ProductsRestService.class));
    }

    /**
     * Every contract carries a type level mapping, which is what the documentation module scans
     * for in the other repository. An interface without one is invisible to it.
     */
    @Test
    void declaresATypeLevelMappingOnEveryContract() {
        for (var contract : CONTRACTS) {
            assertTrue(
                    AnnotatedElementUtils.hasAnnotation(contract, RequestMapping.class),
                    contract.getSimpleName() + " has no type level mapping, so nothing will document it"
            );
        }
    }

    private static List<Class<?>> beanTypes() {
        return Arrays.stream(RestConfiguration.class.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .map(Method::getReturnType)
                .toList();
    }
}
