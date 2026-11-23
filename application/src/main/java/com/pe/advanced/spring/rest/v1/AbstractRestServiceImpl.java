package com.pe.advanced.spring.rest.v1;

import com.pe.advanced.domain.requester.CurrentRequesterProvider;

import java.util.UUID;

public abstract class AbstractRestServiceImpl<ServiceWorker> {

    private final ServiceWorker serviceWorker;
    private final CurrentRequesterProvider currentRequesterProvider;

    protected AbstractRestServiceImpl(ServiceWorker serviceWorker, CurrentRequesterProvider currentRequesterProvider) {
        this.serviceWorker = serviceWorker;
        this.currentRequesterProvider = currentRequesterProvider;
    }

    protected ServiceWorker getServiceWorker() {
        return serviceWorker;
    }

    /**
     * The identification of whoever is making the current request. Resolved here and passed on
     * as an argument, so that nothing behind this layer reads it from an ambient context.
     */
    protected UUID currentRequesterId() {
        return currentRequesterProvider.getCurrent().id();
    }
}
