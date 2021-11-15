package com.example.restreactive.mapping;

import com.example.restreactive.dto.DtoObject;
import com.example.restreactive.model.EntityObject;

public interface DtoMapper {
    boolean accepts(DtoObject dtoObject);

    EntityObject mapTo(DtoObject dtoObject);

    EntityObject updateEntity(EntityObject entityObject, DtoObject dtoObject);
}
