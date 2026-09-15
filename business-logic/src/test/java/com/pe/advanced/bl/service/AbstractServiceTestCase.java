package com.pe.advanced.bl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractServiceTestCase {

    protected UUID requesterId;
    protected UUID alternativeRequesterId;

    @BeforeEach
    protected void setUpAbstractService() {
        this.requesterId = UUID.randomUUID();
        this.alternativeRequesterId = UUID.randomUUID();
    }
}
