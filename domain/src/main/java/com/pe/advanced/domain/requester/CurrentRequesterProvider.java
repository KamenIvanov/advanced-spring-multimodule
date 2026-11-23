package com.pe.advanced.domain.requester;

import com.pe.advanced.domain.exceptions.AuthenticationException;

/**
 * The requester of the current request, so the services take only what their case needs - not where the requester
 * comes from (the gateway's headers).
 */
public interface CurrentRequesterProvider {

    /**
     * @return the current request's requester
     * @throws AuthenticationException when the request carries no usable identification
     */
    Requester getCurrent();
}