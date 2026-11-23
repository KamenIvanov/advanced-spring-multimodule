package com.pe.advanced.spring.transformers;

import com.pe.advanced.api.dto.SortDirectionDto;
import com.pe.advanced.dao.api.search.SortDirection;
import com.pe.advanced.domain.transformers.AbstractEnumTransformer;

public class SortDirectionTransformer extends AbstractEnumTransformer<SortDirection, SortDirectionDto> {

    public static final SortDirectionTransformer instance = new SortDirectionTransformer();

    private SortDirectionTransformer() {
        super(SortDirection.class, SortDirectionDto.class);
    }
}
