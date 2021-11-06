package com.example.restreactive.mapping;

import com.example.restreactive.dto.DtoObject;
import com.example.restreactive.model.EntityObject;

public interface EntityMapper {
    boolean accepts(EntityObject entityObject);
    DtoObject mapTo(EntityObject entityObject);
}
