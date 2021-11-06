package com.example.restreactive.mapping;

import com.example.restreactive.dto.DtoObject;
import com.example.restreactive.dto.UserDto;
import com.example.restreactive.model.EntityObject;
import com.example.restreactive.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModelMapper {

    @Autowired
    private List<DtoMapper> dtoMappers;

    @Autowired
    private List<EntityMapper> entityMappers;

    public DtoObject toDto(EntityObject entityObject) {
        return entityMappers.stream()
            .filter(mapper -> mapper.accepts(entityObject))
            .map(mapper -> mapper.mapTo(entityObject))
            .findAny()
            .orElseThrow(() -> new ApptException("No mapper found " +
                "for entity of type " +
                entityObject.getClass().getName()))
            ;
    }

    public EntityObject toEntity(DtoObject dtoObject) {
        return dtoMappers.stream()
            .filter(mapper -> mapper.accepts(dtoObject))
            .map(mapper -> mapper.mapTo(dtoObject))
            .findAny()
            .orElseThrow(() -> new ApptException("No mapper found " +
                "for dto of type " +
                dtoObject.getClass().getName()))
            ;
    }

    public EntityObject update(EntityObject entityObject,
                               DtoObject dtoObject) {
        return dtoMappers.stream()
            .filter(mapper -> mapper.accepts(dtoObject))
            .map(mapper -> mapper.updateEntity(entityObject, dtoObject))
            .findAny()
            .orElseThrow(() -> new ApptException("No mapper found " +
                "for update of type " +
                dtoObject.getClass().getName()))
            ;
    }

    public EntityObject insert(DtoObject dtoObject) {
        return toEntity(dtoObject);
    }
}
