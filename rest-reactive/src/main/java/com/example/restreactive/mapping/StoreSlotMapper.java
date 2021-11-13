package com.example.restreactive.mapping;

import com.example.restreactive.dto.StoreSlotDto;
import com.example.restreactive.dto.DtoObject;
import com.example.restreactive.model.StoreSlot;
import com.example.restreactive.model.EntityObject;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class StoreSlotMapper implements DtoMapper, EntityMapper {

    @Override
    public boolean accepts(DtoObject dtoObject) {
        return dtoObject instanceof StoreSlotDto;
    }

    @Override
    public EntityObject mapTo(DtoObject dtoObject) {
        return appointmentSlotEntityOf((StoreSlotDto) dtoObject);
    }

    @Override
    public boolean accepts(EntityObject entityObject) {
        return entityObject instanceof StoreSlot;
    }

    @Override
    public DtoObject mapTo(EntityObject entityObject) {
        return appointmentSlotDtoOf((StoreSlot) entityObject);
    }

    @Override
    public EntityObject updateEntity(EntityObject entityObject, DtoObject dtoObject) {
        return appointmentSlotUpdate((StoreSlot) entityObject, (StoreSlotDto) dtoObject);
    }

    public StoreSlot appointmentSlotUpdate(StoreSlot storeSlot, StoreSlotDto storeSlotDto) {
        Integer id = nonNull(storeSlot.getId()) ?
            storeSlot.getId() :
            storeSlotDto.getId();
        String storeCode = nonNull(storeSlotDto.getStoreCode()) ?
            storeSlotDto.getStoreCode() :
            storeSlot.getStoreCode();
        String slotCode = nonNull(storeSlotDto.getSlotCode()) ?
            storeSlotDto.getSlotCode() :
            storeSlot.getSlotCode();
        ZonedDateTime startTime = nonNull(storeSlotDto.getStartTime()) ?
            storeSlotDto.getStartTime() :
            storeSlot.getStartTime();
        ZonedDateTime endTime = nonNull(storeSlotDto.getEndTime()) ?
            storeSlotDto.getEndTime() :
            storeSlot.getEndTime();
        return StoreSlot.builder()
            .id(id)
            .slotCode(slotCode)
            .storeCode(storeCode)
            .startTime(startTime)
            .endTime(endTime)
            .build();
    }

    public StoreSlot appointmentSlotEntityOf(StoreSlotDto storeSlotDto) {
        if (isNull(storeSlotDto)) {
            return null;
        }
        return new StoreSlot(
            storeSlotDto.getId()
            , storeSlotDto.getSlotCode()
            , storeSlotDto.getStoreCode()
            , storeSlotDto.getStartTime()
            , storeSlotDto.getEndTime()
        );
    }

    public StoreSlotDto appointmentSlotDtoOf(StoreSlot storeSlot) {
        if (isNull(storeSlot)) {
            return null;
        }
        return StoreSlotDto.builder()
            .id(storeSlot.getId())
            .slotCode(storeSlot.getSlotCode())
            .storeCode(storeSlot.getStoreCode())
            .startTime(storeSlot.getStartTime())
            .endTime(storeSlot.getEndTime())
            .build();
    }
}
