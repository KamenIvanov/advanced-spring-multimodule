package com.pe.advanced.domain;

import java.util.UUID;

public abstract class AbstractNamedDomain<IdType> extends AbstractUpdatableDomain<IdType> {

    private String name;

    protected AbstractNamedDomain() {
        // POJO
    }

    protected AbstractNamedDomain(IdType id, UUID createdById, String name) {
        super(id, createdById);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
