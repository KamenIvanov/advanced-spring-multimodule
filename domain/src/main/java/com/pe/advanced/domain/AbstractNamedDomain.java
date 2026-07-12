package com.pe.advanced.domain;

public class AbstractNamedDomain<IdType> extends AbstractUpdatableDomain<IdType> {

    private String name;

    public AbstractNamedDomain() {
        // POJO
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
