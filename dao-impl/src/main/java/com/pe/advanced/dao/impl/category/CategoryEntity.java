package com.pe.advanced.dao.impl.category;

import com.pe.advanced.dao.impl.AbstractAuditableEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORIES")
public class CategoryEntity extends AbstractAuditableEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CategoryStatusEntity status;

    public CategoryEntity() {
        // POJO
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryStatusEntity getStatus() {
        return status;
    }

    public void setStatus(CategoryStatusEntity active) {
        this.status = active;
    }
}
