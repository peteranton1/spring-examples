package com.example.restreactive.mapping;

import com.example.restreactive.dto.AppointmentSlotDto;
import com.example.restreactive.dto.DtoObject;
import com.example.restreactive.model.AppointmentSlot;
import com.example.restreactive.model.EntityObject;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class AppointmentSlotMapper implements DtoMapper, EntityMapper {

    @Override
    public boolean accepts(DtoObject dtoObject) {
        return dtoObject instanceof AppointmentSlotDto;
    }

    @Override
    public EntityObject mapTo(DtoObject dtoObject) {
        return appointmentSlotEntityOf((AppointmentSlotDto) dtoObject);
    }

    @Override
    public boolean accepts(EntityObject entityObject) {
        return entityObject instanceof AppointmentSlot;
    }

    @Override
    public DtoObject mapTo(EntityObject entityObject) {
        return appointmentSlotDtoOf((AppointmentSlot) entityObject);
    }

    @Override
    public EntityObject updateEntity(EntityObject entityObject, DtoObject dtoObject) {
        return appointmentSlotUpdate((AppointmentSlot) entityObject, (AppointmentSlotDto) dtoObject);
    }

    public AppointmentSlot appointmentSlotUpdate(AppointmentSlot appointmentSlot, AppointmentSlotDto appointmentSlotDto) {
        Integer id = nonNull(appointmentSlot.getId()) ?
            appointmentSlot.getId() :
            appointmentSlotDto.getId();
        ZonedDateTime startTime = nonNull(appointmentSlotDto.getStartTime()) ?
            appointmentSlotDto.getStartTime() :
            appointmentSlot.getStartTime();
        ZonedDateTime endTime = nonNull(appointmentSlotDto.getEndTime()) ?
            appointmentSlotDto.getEndTime() :
            appointmentSlot.getEndTime();
        return AppointmentSlot.builder()
            .id(id)
            .startTime(startTime)
            .endTime(endTime)
            .build();
    }

    public AppointmentSlot appointmentSlotEntityOf(AppointmentSlotDto appointmentSlotDto) {
        if(isNull(appointmentSlotDto)) {
            return null;
        }
        return new AppointmentSlot(
            appointmentSlotDto.getId()
            , appointmentSlotDto.getStartTime()
            , appointmentSlotDto.getEndTime()
        );
    }

    public AppointmentSlotDto appointmentSlotDtoOf(AppointmentSlot appointmentSlot) {
        if(isNull(appointmentSlot)) {
            return null;
        }
        return AppointmentSlotDto.builder()
            .id(appointmentSlot.getId())
            .startTime(appointmentSlot.getStartTime())
            .endTime(appointmentSlot.getEndTime())
            .build();
    }
}
